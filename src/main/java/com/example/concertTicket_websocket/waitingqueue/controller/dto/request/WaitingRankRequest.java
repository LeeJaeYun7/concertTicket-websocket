package com.example.concertTicket_websocket.waitingqueue.controller.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WaitingRankRequest {

    private String token;

    @Builder
    public WaitingRankRequest(String token){
        this.token = token;
    }
}
