package com.example.concertTicket_websocket.waitingqueue.service;

import com.example.concertTicket_websocket.websocket.infrastructure.HeartbeatDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeartbeatService {

    private final HeartbeatDAO heartbeatDAO;
    // 사용자의 Heartbeat 정보를 최신화하기 위한 기능
    public void updateUserHealthStatus(String token, String timestamp) {
        heartbeatDAO.updateUserHealthStatus(token, timestamp);
    }
}
