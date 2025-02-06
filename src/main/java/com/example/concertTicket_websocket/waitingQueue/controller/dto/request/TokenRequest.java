package com.example.concertTicket_websocket.waitingQueue.controller.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRequest {

    private long concertId;
    private String uuid;

    @Builder
    public TokenRequest(long concertId, String uuid){
        this.concertId = concertId;
        this.uuid = uuid;
    }
}
