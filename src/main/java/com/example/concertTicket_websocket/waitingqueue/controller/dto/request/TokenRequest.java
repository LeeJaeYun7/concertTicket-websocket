package com.example.concertTicket_websocket.waitingqueue.controller.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRequest {

    private String uuid;

    @Builder
    public TokenRequest(String uuid){
        this.uuid = uuid;
    }
}
