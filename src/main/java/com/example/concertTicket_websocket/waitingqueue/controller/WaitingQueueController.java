package com.example.concertTicket_websocket.waitingqueue.controller;

import com.example.concertTicket_websocket.waitingqueue.controller.dto.request.TokenRequest;
import com.example.concertTicket_websocket.waitingqueue.controller.dto.request.WaitingRankRequest;
import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.TokenResponse;
import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.WaitingRankResponse;
import com.example.concertTicket_websocket.waitingqueue.service.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class WaitingQueueController {

    private final WaitingQueueService waitingQueueService;
    @MessageMapping("/v1/waitingQueue/token")
    @SendTo("/topic/token")
    public TokenResponse retrieveToken(TokenRequest tokenRequest, SimpMessageHeaderAccessor headerAccessor) {
        String uuid = tokenRequest.getUuid();

        Principal principal = headerAccessor.getUser();
        String sessionId = principal.getName();

        String token = waitingQueueService.retrieveToken(uuid, sessionId);
        return TokenResponse.of(token);
    }

    @MessageMapping("/v1/waitingQueue/rank")
    @SendTo("/topic/rank")
    public WaitingRankResponse retrieveWaitingRank(WaitingRankRequest waitingRankRequest) {
        String token = waitingRankRequest.getToken();
        return waitingQueueService.retrieveWaitingRank(token);
    }
}
