package com.example.concertTicket_websocket.websocket.infrastructure.enums;

public enum WebsocketInfo {

    TOKEN_DESTINATION("/topic/token"),
    WAITING_QUEUE_STATUS_DESTINATION("/topic/waitingQueue/status");

    private final String value;

    WebsocketInfo(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
