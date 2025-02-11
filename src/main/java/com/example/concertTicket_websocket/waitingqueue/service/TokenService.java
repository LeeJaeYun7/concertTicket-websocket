package com.example.concertTicket_websocket.waitingqueue.service;

import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.TokenActivationResponse;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TokenService {
    private final SimpMessagingTemplate messagingTemplate;
    private final RMap<String, String> tokenSessionIdMap;

    public TokenService(RedissonClient redissonClient, SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        this.tokenSessionIdMap = redissonClient.getMap("tokenSessionId");
    }

    public void sendActivationTokenToClient(List<String> tokens) {

        for (String token : tokens) {
            log.info("Processing token: {}", token);
            String sessionId = tokenSessionIdMap.get(token);
            log.info("User session ID: {}", sessionId);

            if (sessionId != null) {
                TokenActivationResponse response = TokenActivationResponse.activated(token);
                messagingTemplate.convertAndSendToUser(sessionId, "/topic/token", response);
            }
        }
    }
}
