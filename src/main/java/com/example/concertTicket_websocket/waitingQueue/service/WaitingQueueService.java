package com.example.concertTicket_websocket.waitingQueue.service;

import com.example.concertTicket_websocket.redis.WaitingQueueDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final WaitingQueueDao waitingQueueDao;
    public String addToWaitingQueue(long concertId, String uuid){
        return waitingQueueDao.addToWaitingQueue(concertId, uuid);
    }
}
