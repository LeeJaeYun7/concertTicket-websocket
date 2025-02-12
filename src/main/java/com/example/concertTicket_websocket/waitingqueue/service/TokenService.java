package com.example.concertTicket_websocket.waitingqueue.service;

import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.TokenActivationResponse;
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

    public void sendActivatedTokenToClient(String token) {
        log.info("Processing token: {}", token);
        String sessionId = tokenSessionIdMap.get(token);
        log.info("User session ID: {}", sessionId);

        if (sessionId != null) {
            try {
                TokenActivationResponse response = TokenActivationResponse.activated(token);
                messagingTemplate.convertAndSendToUser(sessionId, "/topic/token", response);
                log.info("Successfully sent activation token to user with sessionId: {}", sessionId);

                activatedTokenDao.markTokenAsSent(token);
            } catch (Exception e) {
                log.error("Failed to send activation token to user with sessionId: {}. Error: {}", sessionId, e.getMessage(), e);
            }
        } else {
            log.warn("No session found for token: {}", token);
        }
    }

}
