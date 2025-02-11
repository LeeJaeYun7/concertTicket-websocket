package com.example.concertTicket_websocket.waitingqueue.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TokenActivationResponse {
    private String token;          // 활성화된 토큰
    private String status;         // 상태 (ACTIVE)
    private LocalDateTime activatedAt; // 활성화 시간
    private String message;        // 알림 메시지

    public static TokenActivationResponse activated(String token) {
        return new TokenActivationResponse(
                token,
                "ACTIVE",
                LocalDateTime.now(),
                "토큰이 활성화되었습니다."
        );
    }
}
