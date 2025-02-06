package com.example.concertTicket_websocket.waitingQueue.service;

import com.example.concertTicket_websocket.redis.WaitingQueueDao;
import com.example.concertTicket_websocket.waitingQueue.dto.response.WaitingRankResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final WaitingQueueDao waitingQueueDao;
    public String addToWaitingQueue(long concertId, String uuid){
        return waitingQueueDao.addToWaitingQueue(concertId, uuid);
    }

    public WaitingRankResponse retrieveWaitingRank(long concertId, String uuid) {
        long rank = waitingQueueDao.getWaitingRank(concertId, uuid);

        if(rank == -1){
            return WaitingRankResponse.of(rank, "active");
        }

        return WaitingRankResponse.of(rank, "waiting");
    }
}
