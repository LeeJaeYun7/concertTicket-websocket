package com.example.concertTicket_websocket.websocket.infrastructure;

import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.ActivatedTokenResponse;
import com.example.concertTicket_websocket.waitingqueue.controller.dto.response.WaitingQueueStatusResponse;
import com.example.concertTicket_websocket.websocket.infrastructure.enums.WebsocketInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebsocketClientMessageSender {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendActivatedTokenToClient(String sessionId, ActivatedTokenResponse response) {
        messagingTemplate.convertAndSendToUser(sessionId, WebsocketInfo.TOKEN_DESTINATION, response);
    }

    public void broadcastWaitingQueueStatusToClient(WaitingQueueStatusResponse response){
        messagingTemplate.convertAndSend(WebsocketInfo.WAITING_QUEUE_STATUS_DESTINATION, response);
    }
}
