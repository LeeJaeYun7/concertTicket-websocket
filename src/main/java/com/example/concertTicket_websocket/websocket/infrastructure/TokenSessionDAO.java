package com.example.concertTicket_websocket.websocket.infrastructure;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenSessionDAO {

    private final RedissonClient redissonClient;
    private static final String TOKEN_SESSION_ID_MAP = "tokenSessionId";


    // 토큰을 key로, 세션 ID를 value로 Redis에 저장
    // 토큰-세션 정보를 저장하는 목적은
    // 대기열 -> 활성화열로 토큰이 활성화되었을 때,
    // Websocket 클라이언트에 push 알림을 보내기 위한 정보를 관리하기 위한 목적으로 사용한다.
    public void saveTokenSession(String token, String sessionId) {
        RMap<String, String> sessionsMap = redissonClient.getMap(TOKEN_SESSION_ID_MAP);
        sessionsMap.put(token, sessionId);  // 토큰을 key로, 세션 ID를 value로 저장
    }

    public String getTokenSession(String token) {
        RMap<String, String> sessionsMap = redissonClient.getMap(TOKEN_SESSION_ID_MAP);
        return sessionsMap.get(token);
    }

    public boolean isTokenSessionExists(String token){
        RMap<String, String> sessionsMap = redissonClient.getMap(TOKEN_SESSION_ID_MAP);
        return sessionsMap.containsKey(token);
    }
}
