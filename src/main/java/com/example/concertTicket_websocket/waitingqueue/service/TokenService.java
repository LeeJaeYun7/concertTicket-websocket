package com.example.concertTicket_websocket.waitingqueue.service;

import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.ActivatedTokenResponse;
import com.example.concertTicket_websocket.waitingqueue.infrastructure.ActivatedTokenDao;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RMap<String, String> tokenSessionIdMap;

    private final ActivatedTokenDao activatedTokenDao;

    public TokenService(RedissonClient redissonClient, SimpMessagingTemplate messagingTemplate, ActivatedTokenDao activatedTokenDao) {
        this.messagingTemplate = messagingTemplate;
        this.tokenSessionIdMap = redissonClient.getMap("tokenSessionId");
        this.activatedTokenDao = activatedTokenDao;
    }

    // 사용자가 대기열->활성화열로 이동했을 때, 활성화된 토큰 정보를 클라이언트에 보내는 기능입니다
    // 이 기능은 Broadcast 방식이 아니라,
    // 해당하는 WebSocket 클라이언트에 매칭되는 토큰을 1:1로 보내는 기능입니다.
    public void sendActivatedTokenToClient(String token) {
        log.info("Processing token: {}", token);
        String sessionId = tokenSessionIdMap.get(token);
        log.info("User session ID: {}", sessionId);

        if (sessionId != null) {
            try {
                ActivatedTokenResponse response = ActivatedTokenResponse.activated(token);
                messagingTemplate.convertAndSendToUser(sessionId, "/topic/token", response);
                log.info("Successfully sent activated token to user with sessionId: {}", sessionId);

                activatedTokenDao.markTokenAsSent(token);
            } catch (Exception e) {
                log.error("Failed to send activated token to user with sessionId: {}. Error: {}", sessionId, e.getMessage(), e);
            }
        } else {
            log.warn("No session found for token: {}", token);
        }
    }
}
