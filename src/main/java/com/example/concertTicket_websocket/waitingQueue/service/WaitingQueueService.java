package com.example.concertTicket_websocket.waitingQueue.service;

import com.example.concertTicket_websocket.waitingQueue.controller.dto.response.WaitingRankResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.concertTicket_websocket.waitingQueue.controller.dto.response.TokenResponse;
import com.example.concertTicket_websocket.waitingQueue.service.feign.ConcertWaitingQueueClient;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final ConcertWaitingQueueClient concertWaitingQueueClient;

    // 대기열에 추가하고 토큰 반환
    public String addToWaitingQueue(long concertId, String uuid) {
        TokenResponse response = concertWaitingQueueClient.retrieveToken(concertId, uuid);
        return response.getToken();
    }

    // 대기열 순위 조회
    public WaitingRankResponse retrieveWaitingRank(long concertId, String token) {
        return concertWaitingQueueClient.retrieveWaitingRank(concertId, token);
    }
}
