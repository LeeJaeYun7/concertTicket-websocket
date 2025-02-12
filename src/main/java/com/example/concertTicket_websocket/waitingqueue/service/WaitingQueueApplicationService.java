package com.example.concertTicket_websocket.waitingqueue.service;

import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.WaitingRankResponse;
import com.example.concertTicket_websocket.websocket.infrastructure.WebSocketSessionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.TokenResponse;
import com.example.concertTicket_websocket.waitingqueue.service.feign.ConcertWaitingQueueClient;

@Service
@RequiredArgsConstructor
public class WaitingQueueApplicationService {

    private final ConcertWaitingQueueClient concertWaitingQueueClient;
    private final WebSocketSessionDao webSocketSessionDao;

    // 대기열에 추가하고 토큰 반환
    public String addToWaitingQueue(String uuid, String sessionId) {
        TokenResponse response = concertWaitingQueueClient.retrieveToken(uuid);
        webSocketSessionDao.saveSession(response.getToken(), sessionId);
        return response.getToken();
    }

    // 대기열 순위 조회
    public WaitingRankResponse retrieveWaitingRank(String token) {
        return concertWaitingQueueClient.retrieveWaitingRank(token);
    }
}
