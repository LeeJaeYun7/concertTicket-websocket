package com.example.concertTicket_websocket.websocket.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebsocketEventListener {

    private final TokenSessionManager tokenSessionManager;

    // WebSocket 연결 종료 시 세션을 ConcurrentHashMap에서 제거
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        // STOMP 세션 ID를 가져옵니다.
        String sessionId = headerAccessor.getUser().getName();
        tokenSessionManager.removeTokenBySessionId(sessionId);  // ConcurrentHashMap에서 세션 제거
        log.info("WebSocket session disconnected: {}", sessionId);
    }
}
