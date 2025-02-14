package com.example.concertTicket_websocket.websocket.infrastructure;

import com.example.concertTicket_websocket.waitingqueue.infrastructure.ActivatedTokenDao;
import com.example.concertTicket_websocket.waitingqueue.service.TokenService;
import com.example.concertTicket_websocket.waitingqueue.service.WaitingQueueService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class RedisPubSubListener {
    private final RedissonClient redissonClient;
    private static final String TOKEN_PUB_SUB_CHANNEL = "tokenChannel";
    private static final String WAITING_QUEUE_STATUS_PUB_SUB_CHANNEL = "waitingQueueStatusChannel";

    private static final String TOKEN_PROCESSING_LOCK_KEY = "tokenProcessingLock";
    private static final String WAITING_QUEUE_STATUS_LOCK_KEY = "waitingQueueStatusLock";

    private final TokenService tokenService;
    private final WaitingQueueService waitingQueueService;
    private final ActivatedTokenDao activatedTokenDao;

    public RedisPubSubListener(RedissonClient redissonClient, TokenService tokenService, WaitingQueueService waitingQueueService, ActivatedTokenDao activatedTokenDao) {
        this.redissonClient = redissonClient;
        this.tokenService = tokenService;
        this.waitingQueueService = waitingQueueService;
        this.activatedTokenDao = activatedTokenDao;
        startTokenChannelListening();
        startWaitingQueueStatusChannelListening();
    }

    // 활성화된 토큰 정보를 Redis Pub/Sub 채널을 통해 수신하는 기능
    // 웹소켓 서버가 이중화 되었음을 고려해, Redis 분산락을 통해서 1번만 실행되도록 보장합니다
    // 사용자의 Websocket 클라이언트에 해당하는 토큰을 매칭하여 발송합니다.
    private void startTokenChannelListening() {
        // Redis 채널을 구독하기 위한 RTopic 객체
        RTopic topic = redissonClient.getTopic(TOKEN_PUB_SUB_CHANNEL);

        // 메시지 리스너 등록
        topic.addListener(String.class, (channel, message) -> {
                log.info("Received message: {}", message);
                List<String> tokens = parseMessage(message);

                RLock lock = redissonClient.getLock(TOKEN_PROCESSING_LOCK_KEY);

                log.info("Attempting to acquire lock for token processing...");
                lock.lock();
                log.info("Lock Acquired for token processing");

                try {
                    for (String token : tokens) {
                        if (!activatedTokenDao.isTokenAlreadySent(token)) {
                            tokenService.sendActivatedTokenToClient(token);
                        }
                    }
                }finally{
                    lock.unlock();
                    log.info("Lock unlocked for token processing");
                }
        });
        log.info("Started listening on Redis Pub/Sub channel: {}", TOKEN_PUB_SUB_CHANNEL);
    };

    private List<String> parseMessage(String message) {
        // 메시지 형식에 맞게 파싱 로직 작성
        // 예시: 메시지가 `,`로 구분된 토큰 리스트 형태라면 split을 사용하여 리스트로 반환
        message = message.substring(1, message.length()-1);
        return List.of(message.split(",")).stream()
                                                .map(String::trim)
                                                .toList();
    }

    // 대기열 활성화 상태와 관련된 정보를 Redis Pub/Sub 채널을 통해 수신하는 메소드
    // 웹소켓 서버가 이중화 되었음을 고려해, Redis 분산락을 통해서 1번만 발송되도록 했다
    // 전체 Websocket 클라이언트에 대기열 활성화 상태를 Broadcast한다.
    private void startWaitingQueueStatusChannelListening() {
        // Redis 채널을 구독하기 위한 RTopic 객체
        RTopic topic = redissonClient.getTopic(WAITING_QUEUE_STATUS_PUB_SUB_CHANNEL);

        // 메시지 리스너 등록
        topic.addListener(String.class, (channel, message) -> {

            // 메시지는 "active" or "inactive"이다.
            // 이 메시지는 대기열 활성화 상태가 '변경'되었을 때만 수신한다.
            log.info("Received message: {}", message);

            RLock lock = redissonClient.getLock(WAITING_QUEUE_STATUS_LOCK_KEY);

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
        log.info("Started listening on Redis Pub/Sub channel: {}", TOKEN_PUB_SUB_CHANNEL);
    };
}
