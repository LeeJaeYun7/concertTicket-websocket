package com.example.concertTicket_websocket.websocket.infrastructure;

import com.example.concertTicket_websocket.websocket.infrastructure.enums.RedisKey;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class HeartbeatDAO {

    private final RedissonClient redissonClient;

    public boolean isTokenTimestampExists(String token) {
        RMapCache<String, String> heartbeatMap = redissonClient.getMapCache(RedisKey.HEARTBEAT_HASH_KEY);
        return heartbeatMap.containsKey(token);
    }
    

    // 사용자의 Heartbeat 정보를 최신화하기 위한 기능
    public void updateUserHealthStatus(String token, String timestamp) {
        RMapCache<String, String> heartbeatMap = redissonClient.getMapCache(RedisKey.HEARTBEAT_HASH_KEY);
        heartbeatMap.put(token, timestamp, 30, TimeUnit.MINUTES);
    }
}
