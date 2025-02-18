package com.example.concertTicket_websocket.waitingqueue.controller;

import com.example.concertTicket_websocket.waitingqueue.controller.dto.request.ReconnectRequest;
import com.example.concertTicket_websocket.waitingqueue.controller.dto.request.TokenRequest;
import com.example.concertTicket_websocket.waitingqueue.controller.dto.request.WaitingRankRequest;
import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.ReconnectResponse;
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
        String sessionId = headerAccessor.getSessionId();

        String token = waitingQueueService.retrieveToken(uuid, sessionId);
        String waitingQueueStatus = waitingQueueService.retrieveWaitingQueueStatus();

        WaitingRankResponse waitingRankResponse = waitingQueueService.retrieveWaitingRank(token);
        return TokenResponse.of(token, waitingQueueStatus, waitingRankResponse.getWaitingRank());
    }

    @MessageMapping("/v1/waitingQueue/reconnect")
    @SendTo("/topic/reconnect")
    public ReconnectResponse reConnect(ReconnectRequest reconnectRequest, SimpMessageHeaderAccessor headerAccessor) {
        String token = reconnectRequest.getToken();

        Principal principal = headerAccessor.getUser();
        String sessionId = principal.getName();

        boolean result = waitingQueueService.reconnect(token, sessionId);
        return ReconnectResponse.of(result);
    }

    @MessageMapping("/v1/waitingQueue/rank")
    @SendTo("/topic/rank")
    public WaitingRankResponse retrieveWaitingRank(WaitingRankRequest waitingRankRequest) {
        String token = waitingRankRequest.getToken();
        return waitingQueueService.retrieveWaitingRank(token);
    }
}
