package com.example.concertTicket_websocket.waitingqueue.infrastructure.enums;

public enum RedisKey {

    TOKEN_PUB_SUB_CHANNEL("tokenChannel"),
    WAITING_QUEUE_STATUS_PUB_SUB_CHANNEL("waitingQueueStatusChannel"),
    TOKEN_PROCESSING_LOCK_KEY("tokenProcessingLock"),

    WAITING_QUEUE_STATUS_LOCK_KEY("waitingQueueStatusLock");

    private final String key;

    RedisKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
