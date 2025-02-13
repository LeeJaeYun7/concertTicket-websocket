package com.example.concertTicket_websocket.waitingqueue.service;

import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.ActivatedTokenResponse;
import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.WaitingQueueStatusResponse;
import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.WaitingRankResponse;
import com.example.concertTicket_websocket.websocket.infrastructure.WebSocketSessionDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.TokenResponse;
import com.example.concertTicket_websocket.waitingqueue.service.feign.ConcertWaitingQueueClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class WaitingQueueService {

    private final ConcertWaitingQueueClient concertWaitingQueueClient;
    private final WebSocketSessionDao webSocketSessionDao;
    private final SimpMessagingTemplate messagingTemplate;

    // 토큰 반환
    public String retrieveToken(String uuid, String sessionId) {
        TokenResponse response = concertWaitingQueueClient.retrieveToken(uuid);
        webSocketSessionDao.saveSession(response.getToken(), sessionId);
        return response.getToken();
    }

    // 대기열 순위 조회
    public WaitingRankResponse retrieveWaitingRank(String token) {
        return concertWaitingQueueClient.retrieveWaitingRank(token);
    }

    // 대기열의 상태가 변경되었을 때(ex) 활성화 -> 비활성화 or 비활성화 -> 활성화)
    // 해당 정보를 전체 Websocket 클라이언트에 broadcast 합니다
    public void broadcastWaitingQueueStatusToClient(String waitingQueueStatus) {
            try {
                WaitingQueueStatusResponse response = WaitingQueueStatusResponse.of(waitingQueueStatus);
                messagingTemplate.convertAndSend("/topic/token", response);
                log.info("Successfully sent waiting queue status");
            } catch (Exception e) {
                log.error("Failed to send waiting queue status, {}", e.getMessage(), e);
            }
    }
}
