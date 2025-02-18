package com.example.concertTicket_websocket.websocket.infrastructure.enums;

public interface WebsocketInfo {
    String TOKEN_DESTINATION = "/topic/token";

    String WAITING_RANK_DESTINATION = "/topic/rank";
    String WAITING_QUEUE_STATUS_DESTINATION = "/topic/waitingQueue/status";
}
