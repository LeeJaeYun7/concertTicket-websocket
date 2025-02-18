package com.example.concertTicket_websocket.waitingqueue.infrastructure;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class WaitingTokenDAO {

    private final RedissonClient redissonClient;

    private static final String WAITING_TOKENS_KEY = "waitingTokens";

    public boolean isTokenAlreadySent(String token) {
        RMapCache<String, String> waitingTokensMap = redissonClient.getMapCache(WAITING_TOKENS_KEY);
        return waitingTokensMap.containsKey(token);
    }

    public void markTokenAsSent(String token) {
        long now = System.currentTimeMillis();
        RMapCache<String, String> waitingTokensMap = redissonClient.getMapCache(WAITING_TOKENS_KEY);
        waitingTokensMap.put(token, Long.toString(now), 5, TimeUnit.SECONDS);
    }
}
