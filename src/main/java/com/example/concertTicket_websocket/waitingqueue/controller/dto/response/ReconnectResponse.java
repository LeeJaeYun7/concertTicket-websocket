package com.example.concertTicket_websocket.waitingqueue.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReconnectResponse {

    private boolean result;


    @Builder
    public ReconnectResponse(boolean result){
        this.result = result;
    }

    public static ReconnectResponse of(boolean result){
        return ReconnectResponse.builder()
                            .result(result)
                            .build();
    }
}
