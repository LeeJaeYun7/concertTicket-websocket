package com.example.concertTicket_websocket.websocket.infrastructure;

import com.example.concertTicket_websocket.websocket.infrastructure.enums.RedisKey;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HeartbeatDAO {

    private final RedissonClient redissonClient;
    

    // 사용자의 Heartbeat 정보를 최신화하기 위한 기능
    public void updateUserHealthStatus(String token, String timestamp) {
        RMap<String, String> heartbeatMap = redissonClient.getMap(RedisKey.HEARTBEAT_HASH_KEY);
        heartbeatMap.put(token, timestamp);
    }
}
