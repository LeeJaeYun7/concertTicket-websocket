package com.example.concertTicket_websocket.waitingqueue.infrastructure;

import com.example.concertTicket_websocket.websocket.infrastructure.enums.RedisKey;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingQueueStatusDAO {

    private final RedissonClient redisson;

    public String getWaitingQueueStatus() {
        RMap<String, String> waitingQueueStatusMap = redisson.getMap(RedisKey.WAITING_QUEUE_STATUS);
        return waitingQueueStatusMap.getOrDefault("status", "inactive");
    }
}
