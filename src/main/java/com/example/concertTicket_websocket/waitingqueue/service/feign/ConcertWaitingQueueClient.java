package com.example.concertTicket_websocket.waitingqueue.service.feign;

import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.TokenResponse;
import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.WaitingRankResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name= "concertWaitingQueueClient", url = "${concertticket.server.url}")
public interface ConcertWaitingQueueClient {
    @GetMapping("/api/v1/waitingQueue/token")
    TokenResponse retrieveToken(@RequestParam("uuid") String uuid);

    // 대기열 순위를 조회하는 API
    @GetMapping("/api/v1/waitingQueue/rank")
    WaitingRankResponse retrieveWaitingRank(@RequestParam("token") String token);
}