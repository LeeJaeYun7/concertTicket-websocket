package com.example.concertTicket_websocket.waitingqueue.controller.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HeartbeatRequest {

    private String token;
    private String timestamp;

    @Builder
    public HeartbeatRequest(String token, String timestamp){
        this.token = token;
        this.timestamp = timestamp;
    }
}
