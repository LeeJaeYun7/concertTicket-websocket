package com.example.concertTicket_websocket.waitingqueue.infrastructure;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivatedTokenDao {

    private final RedissonClient redissonClient;

    private static final String ACTIVATED_TOKENS_KEY = "activatedTokens";

    public boolean isTokenAlreadySent(String token) {
        RSet<String> activatedTokensSet = redissonClient.getSet(ACTIVATED_TOKENS_KEY);
        return activatedTokensSet.contains(token);
    }

    public void markTokenAsSent(String token) {
        // 발송된 토큰을 Redis Set에 추가
        RSet<String> activatedTokensSet = redissonClient.getSet(ACTIVATED_TOKENS_KEY);
        activatedTokensSet.add(token);
    }
}
