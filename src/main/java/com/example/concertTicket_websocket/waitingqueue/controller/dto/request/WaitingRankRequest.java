package com.example.concertTicket_websocket.waitingqueue.controller.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WaitingRankRequest {

    private long concertId;
    private String token;

    @Builder
    public WaitingRankRequest(long concertId, String token){
        this.concertId = concertId;
        this.token = token;
    }
}
