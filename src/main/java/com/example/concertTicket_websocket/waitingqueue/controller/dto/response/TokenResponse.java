package com.example.concertTicket_websocket.waitingqueue.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenResponse {

    private String token;
    private String waitingQueueStatus;
    private long waitingRank;

    @Builder
    public TokenResponse(String token, String waitingQueueStatus, long waitingRank){
        this.token = token;
        this.waitingQueueStatus = waitingQueueStatus;
        this.waitingRank = waitingRank;
    }

    public static TokenResponse of(String token, String waitingQueueStatus, long waitingRank){
        return TokenResponse.builder()
                            .token(token)
                            .waitingQueueStatus(waitingQueueStatus)
                            .waitingRank(waitingRank)
                            .build();
    }
}
