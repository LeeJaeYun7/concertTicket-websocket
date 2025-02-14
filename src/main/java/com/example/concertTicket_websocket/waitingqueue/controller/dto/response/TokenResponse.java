package com.example.concertTicket_websocket.waitingqueue.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenResponse {

    private String token;

    @Builder
    public TokenResponse(String token){
        this.token = token;
    }

    public static TokenResponse of(String token){
        return TokenResponse.builder()
                            .token(token)
                            .build();
    }
}
