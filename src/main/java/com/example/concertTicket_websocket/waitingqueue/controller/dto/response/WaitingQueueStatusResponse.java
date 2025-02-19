package com.example.concertTicket_websocket.waitingqueue.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WaitingQueueStatusResponse {

    private String status;

    @Builder
    public WaitingQueueStatusResponse(String status){
        this.status = status;
    }

    public static WaitingQueueStatusResponse of(String status){
        return WaitingQueueStatusResponse.builder()
                                         .status(status)
                                         .build();
    }
}
