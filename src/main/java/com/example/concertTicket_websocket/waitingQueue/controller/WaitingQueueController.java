package com.example.concertTicket_websocket.waitingQueue.controller;

import com.example.concertTicket_websocket.waitingQueue.dto.request.TokenRequest;
import com.example.concertTicket_websocket.waitingQueue.dto.response.TokenResponse;
import com.example.concertTicket_websocket.waitingQueue.service.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WaitingQueueController {

    private final WaitingQueueService waitingQueueService;
    @MessageMapping("/waitingQueue/token")
    @SendTo("/topic/token")
    public TokenResponse retrieveToken(TokenRequest tokenRequest) throws Exception {
        System.out.println(tokenRequest.getUuid());
        long concertId = tokenRequest.getConcertId();
        String uuid = tokenRequest.getUuid();

        String token = waitingQueueService.addToWaitingQueue(concertId, uuid);
        return TokenResponse.of(token);
    }
}
