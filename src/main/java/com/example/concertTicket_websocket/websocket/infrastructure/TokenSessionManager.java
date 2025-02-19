package com.example.concertTicket_websocket.websocket.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenSessionManager {

    private final Map<String, String> tokenSessionMap = new ConcurrentHashMap<>();

    public void saveTokenSession(String token, String sessionId) {
        tokenSessionMap.put(token, sessionId);
        log.info("token, sessionId is saved, {}, {}", token, sessionId);
    }

    public boolean isExistsToken(String token) {
        return tokenSessionMap.containsKey(token);
    }

    public String getSessionIdByToken(String token) {
        return tokenSessionMap.get(token);
    }

    public void removeTokenBySessionId(String sessionId) {
        tokenSessionMap.keySet().removeIf(key -> tokenSessionMap.get(key).equals(sessionId));
    }
}
