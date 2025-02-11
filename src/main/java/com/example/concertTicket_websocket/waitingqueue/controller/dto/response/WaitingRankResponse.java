package com.example.concertTicket_websocket.waitingqueue.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WaitingRankResponse {

    private long waitingRank;
    private String status;

    @Builder
    public WaitingRankResponse(long waitingRank, String status){
        this.waitingRank = waitingRank;
        this.status = status;
    }

    public static WaitingRankResponse of(long waitingRank, String status){
        return WaitingRankResponse.builder()
                .waitingRank(waitingRank)
                .status(status)
                .build();
    }
}
