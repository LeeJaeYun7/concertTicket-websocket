package com.example.concertTicket_websocket.websocket.infrastructure;

import com.example.concertTicket_websocket.waitingqueue.infrastructure.ActivatedTokenDao;
import com.example.concertTicket_websocket.websocket.dto.WaitingDTO;
import com.example.concertTicket_websocket.websocket.infrastructure.enums.RedisKey;
import com.example.concertTicket_websocket.waitingqueue.service.TokenService;
import com.example.concertTicket_websocket.waitingqueue.service.WaitingQueueService;
import com.example.concertTicket_websocket.websocket.utils.MessageParser;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisPubSubListener {

    private final RedissonClient redissonClient;
    private final TokenService tokenService;
    private final WaitingQueueService waitingQueueService;
    private final ActivatedTokenDao activatedTokenDao;
    private final MessageParser messageParser;

    @PostConstruct
    public void init() {
        try {
            startWaitingQueueStatusChannelListening();
            startActiveTokenChannelListening();
            startWaitingTokenChannelListening();
        } catch (Exception e) {
            log.error("Error during Redis Pub/Sub channel initialization", e);
        }
    }

    // 활성화된 토큰 정보를 Redis Pub/Sub 채널을 통해 수신하는 기능
    // 웹소켓 서버가 이중화 되었음을 고려해, Redis 분산락을 통해서 1번만 실행되도록 보장합니다
    // 사용자의 Websocket 클라이언트에 해당하는 활성화된 토큰을 매칭하여 발송합니다.
    private void startActiveTokenChannelListening() {
        // Redis 채널을 구독하기 위한 RTopic 객체
        RTopic topic = redissonClient.getTopic(RedisKey.ACTIVE_TOKEN_PUB_SUB_CHANNEL);

        // 메시지 리스너 등록
        topic.addListener(String.class, (channel, message) -> {
                log.info("Received message: {}", message);
                List<String> tokens = messageParser.parseActiveTokenMessage(message);

                RLock lock = redissonClient.getLock(RedisKey.ACTIVE_TOKEN_PROCESSING_LOCK_KEY);

                log.info("Attempting to acquire lock for active token processing...");
                lock.lock();
                log.info("Lock Acquired for active token processing");

                for (String token : tokens) {
                    if (!activatedTokenDao.isTokenAlreadySent(token)) {
                        tokenService.sendActivatedTokenToClient(token);
                    }
                }
                lock.unlock();
                log.info("Lock unlocked for active token processing");
        });
        log.info("Started listening on Redis Pub/Sub channel: {}", RedisKey.ACTIVE_TOKEN_PUB_SUB_CHANNEL);
    };

    // 대기열에 있는 토큰 정보를 Redis Pub/Sub 채널을 통해 수신하는 기능
    // 웹소켓 서버가 이중화 되었음을 고려해, Redis 분산락을 통해서 1번만 실행되도록 보장합니다
    // 사용자의 Websocket 클라이언트에 해당하는 대기열 토큰을 매칭하여 발송합니다.

    private void startWaitingTokenChannelListening() {
        // Redis 채널을 구독하기 위한 RTopic 객체
        RTopic topic = redissonClient.getTopic(RedisKey.WAITING_TOKEN_PUB_SUB_CHANNEL);

        // 메시지 리스너 등록
        topic.addListener(String.class, (channel, message) -> {
            log.info("Received message: {}", message);
            List<WaitingDTO> tokens = messageParser.parseWaitingTokenMessage(message);

            RLock lock = redissonClient.getLock(RedisKey.WAITING_TOKEN_PROCESSING_LOCK_KEY);

            log.info("Attempting to acquire lock for waiting token processing...");
            lock.lock();
            log.info("Lock Acquired for waiting token processing");
            /*
            for (WaitingDTO token : tokens) {
                if (!activatedTokenDao.isTokenAlreadySent(token)) {
                    tokenService.sendWaitingTokenToClient(token);
                }
            }
            */
            lock.unlock();
            log.info("Lock unlocked for waiting token processing");
        });
        log.info("Started listening on Redis Pub/Sub channel: {}", RedisKey.WAITING_TOKEN_PUB_SUB_CHANNEL);
    };


    // 대기열 활성화 상태와 관련된 정보를 Redis Pub/Sub 채널을 통해 수신하는 메소드
    // 웹소켓 서버가 이중화 되었음을 고려해, Redis 분산락을 통해서 1번만 발송되도록 했다
    // 전체 Websocket 클라이언트에 대기열 활성화 상태를 Broadcast한다.
    private void startWaitingQueueStatusChannelListening() {
        // Redis 채널을 구독하기 위한 RTopic 객체
        RTopic topic = redissonClient.getTopic(RedisKey.WAITING_QUEUE_STATUS_PUB_SUB_CHANNEL);

        // 메시지 리스너 등록
        topic.addListener(String.class, (channel, message) -> {

            // 메시지는 "active" or "inactive"이다.
            // 이 메시지는 대기열 활성화 상태가 '변경'되었을 때만 수신한다.
            log.info("Received message: {}", message);

            RLock lock = redissonClient.getLock(RedisKey.WAITING_QUEUE_STATUS_LOCK_KEY);

            log.info("Attempting to acquire lock for waiting queue status...");
            lock.lock();
            log.info("Lock Acquired for waiting queue status");

            try {
                waitingQueueService.broadcastWaitingQueueStatusToClient(message);
            }finally{
                lock.unlock();
                log.info("Lock unlocked for waiting queue status");
            }
        });
        log.info("Started listening on Redis Pub/Sub channel: {}", RedisKey.WAITING_QUEUE_STATUS_PUB_SUB_CHANNEL);
    };
}
