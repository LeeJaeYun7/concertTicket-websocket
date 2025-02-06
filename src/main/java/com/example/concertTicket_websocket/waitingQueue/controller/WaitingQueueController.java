package com.example.concertTicket_websocket.waitingQueue.controller;

import com.example.concertTicket_websocket.waitingQueue.controller.dto.request.TokenRequest;
import com.example.concertTicket_websocket.waitingQueue.controller.dto.request.WaitingRankRequest;
import com.example.concertTicket_websocket.waitingQueue.controller.dto.response.TokenResponse;
import com.example.concertTicket_websocket.waitingQueue.controller.dto.response.WaitingRankResponse;
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
    public TokenResponse retrieveToken(TokenRequest tokenRequest) {
        long concertId = tokenRequest.getConcertId();
        String uuid = tokenRequest.getUuid();

        String token = waitingQueueService.addToWaitingQueue(concertId, uuid);
        return TokenResponse.of(token);
    }

    @MessageMapping("/waitingQueue/rank")
    @SendTo("/topic/rank")
    public WaitingRankResponse retrieveWaitingRank(WaitingRankRequest waitingRankRequest) {
        long concertId = waitingRankRequest.getConcertId();
        String token = waitingRankRequest.getToken();
        return waitingQueueService.retrieveWaitingRank(concertId, token);
    }
}
