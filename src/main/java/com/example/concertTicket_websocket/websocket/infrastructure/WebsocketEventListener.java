package com.example.concertTicket_websocket.websocket.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
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
        String sessionId = event.getSessionId();
        tokenSessionManager.removeTokenBySessionId(sessionId);  // ConcurrentHashMap에서 세션 제거
        log.info("WebSocket session disconnected: {}", sessionId);
    }
}
