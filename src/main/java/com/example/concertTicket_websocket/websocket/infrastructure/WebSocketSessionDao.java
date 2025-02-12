package com.example.concertTicket_websocket.websocket.infrastructure;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class WebSocketSessionDao {

    private final RedissonClient redissonClient;
    private static final String TOKEN_SESSION_ID_MAP = "tokenSessionId";


    // 토큰을 key로, 세션 ID를 value로 Redis에 저장
    public void saveSession(String token, String sessionId) {
        RMap<String, String> sessionsMap = redissonClient.getMap(TOKEN_SESSION_ID_MAP);
        sessionsMap.put(token, sessionId);  // 토큰을 key로, 세션 ID를 value로 저장
    }

    // 세션 정보 삭제 (토큰을 사용하여 세션 삭제)
    public void removeSession(String token) {
        RMap<String, String> sessionsMap = redissonClient.getMap(TOKEN_SESSION_ID_MAP);
        sessionsMap.remove(token);  // 해당 토큰에 대한 세션 정보 삭제
    }

    // 토큰을 사용하여 세션 ID 조회
    public String getSession(String token) {
        RMap<String, String> sessionsMap = redissonClient.getMap(TOKEN_SESSION_ID_MAP);
        return sessionsMap.get(token);  // 토큰에 해당하는 세션 ID 조회
    }
}
