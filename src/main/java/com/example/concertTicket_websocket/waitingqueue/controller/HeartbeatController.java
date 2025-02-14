package com.example.concertTicket_websocket.waitingqueue.controller;

import com.example.concertTicket_websocket.waitingqueue.controller.dto.request.HeartbeatRequest;
import com.example.concertTicket_websocket.waitingqueue.service.HeartbeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HeartbeatController {

    private final HeartbeatService heartbeatService;

    @MessageMapping("/v1/heartbeat")
    public void receiveHeartbeat(HeartbeatRequest heartbeatRequest, SimpMessageHeaderAccessor headerAccessor) {
        Principal principal = headerAccessor.getUser();
        String sessionId = principal.getName();

        String token = heartbeatRequest.getToken();
        String timestamp = heartbeatRequest.getTimestamp();

        log.info("Received Heartbeat from session, {}", sessionId);
        heartbeatService.updateUserHealthStatus(token, timestamp);
    }
}
