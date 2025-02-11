package com.example.concertTicket_websocket.websocket.infrastructure;

import com.example.concertTicket_websocket.waitingqueue.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RedisPubSubListener {
    private final RedissonClient redissonClient;
    private static final String PUB_SUB_CHANNEL = "tokenChannel";
    private final TokenService tokenService;

    public RedisPubSubListener(RedissonClient redissonClient, TokenService tokenService) {
        this.redissonClient = redissonClient;
        this.tokenService = tokenService;
        startListening();
    }

    private void startListening() {
        // Redis 채널을 구독하기 위한 RTopic 객체
        RTopic topic = redissonClient.getTopic(PUB_SUB_CHANNEL);

        // 메시지 리스너 등록
        topic.addListener(String.class, (channel, message) -> {
                log.info("Received message: {}", message);
                List<String> tokens = parseMessage(message);
                tokenService.sendActivationTokenToClient(tokens);
        });
        log.info("Started listening on Redis Pub/Sub channel: {}", PUB_SUB_CHANNEL);
    };

    private List<String> parseMessage(String message) {
        // 메시지 형식에 맞게 파싱 로직 작성
        // 예시: 메시지가 `,`로 구분된 토큰 리스트 형태라면 split을 사용하여 리스트로 반환
        message = message.substring(1, message.length()-1);
        return List.of(message.split(",")).stream()
                                                .map(String::trim)
                                                .toList();
    }
}
