package com.example.concertTicket_websocket.waitingqueue.service;

import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.ActivatedTokenResponse;
import com.example.concertTicket_websocket.waitingqueue.infrastructure.ActivatedTokenDao;
import com.example.concertTicket_websocket.websocket.infrastructure.TokenSessionDAO;
import com.example.concertTicket_websocket.websocket.infrastructure.WebsocketClientMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    private final WebsocketClientMessageSender websocketClientMessageSender;
    private final ActivatedTokenDao activatedTokenDao;
    private final TokenSessionDAO tokenSessionDAO;

    // 사용자가 대기열->활성화열로 이동했을 때, 활성화된 토큰 정보를 클라이언트에 보내는 기능입니다
    // 이 기능은 Broadcast 방식이 아니라,
    // 해당하는 WebSocket 클라이언트에 매칭되는 토큰을 1:1로 보내는 기능입니다.
    public void sendActivatedTokenToClient(String token) {
        log.info("Processing token: {}", token);
        String sessionId = tokenSessionDAO.getTokenSession(token);
        log.info("User session ID: {}", sessionId);

        if (null == sessionId) {
            log.warn("No session found for token: {}", token);
            return;
        }

        try {
            ActivatedTokenResponse response = ActivatedTokenResponse.activated(token);
            websocketClientMessageSender.sendActivatedTokenToClient(sessionId, response);

            log.info("Successfully sent activated token to user with sessionId: {}", sessionId);
            activatedTokenDao.markTokenAsSent(token);
        } catch (Exception e) {
            log.error("Failed to send activated token to user with sessionId: {}. Error: {}", sessionId, e.getMessage(), e);
        }
    }
}
