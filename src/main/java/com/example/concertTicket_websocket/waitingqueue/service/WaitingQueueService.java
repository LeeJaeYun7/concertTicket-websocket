package com.example.concertTicket_websocket.waitingqueue.service;

import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.WaitingQueueStatusResponse;
import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.WaitingRankResponse;
import com.example.concertTicket_websocket.websocket.infrastructure.TokenSessionDAO;
import com.example.concertTicket_websocket.websocket.infrastructure.WebsocketClientMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.TokenResponse;
import com.example.concertTicket_websocket.waitingqueue.service.feign.ConcertWaitingQueueClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class WaitingQueueService {

    private final ConcertWaitingQueueClient concertWaitingQueueClient;
    private final TokenSessionDAO tokenSessionDao;
    private final WebsocketClientMessageSender websocketClientMessageSender;

    // 토큰 반환
    public String retrieveToken(String uuid, String sessionId) {
        TokenResponse response = concertWaitingQueueClient.retrieveToken(uuid);
        tokenSessionDao.saveTokenSession(response.getToken(), sessionId);
        return response.getToken();
    }

    public boolean reconnect(String token, String sessionId){
        boolean isExist = tokenSessionDao.isTokenSessionExists(token);

        if (!isExist) {
            return false;
        }

        tokenSessionDao.saveTokenSession(token, sessionId);
        return true;
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
            websocketClientMessageSender.broadcastWaitingQueueStatusToClient(response);
            log.info("Successfully sent waiting queue status");
        } catch (Exception e) {
            log.error("Failed to send waiting queue status, {}", e.getMessage(), e);
        }
    }
}
