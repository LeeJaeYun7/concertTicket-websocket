package com.example.concertTicket_websocket.waitingqueue.infrastructure;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ActivatedTokenDAO {

    private final RedissonClient redissonClient;

    private static final String ACTIVATED_TOKENS_KEY = "activatedTokens";

    public boolean isTokenAlreadySent(String token) {
        RMapCache<String, String> activatedTokensMap = redissonClient.getMapCache(ACTIVATED_TOKENS_KEY);
        return activatedTokensMap.containsKey(token);
    }

    public void markTokenAsSent(String token) {
        // 발송된 토큰을 Redis Set에 추가
        RMapCache<String, String> activatedTokensMap = redissonClient.getMapCache(ACTIVATED_TOKENS_KEY);
        activatedTokensMap.put(token, token, 5, TimeUnit.SECONDS);
    }
}
