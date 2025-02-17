package com.example.concertTicket_websocket.websocket.infrastructure.enums;

public interface RedisKey {
    String ACTIVE_TOKEN_PUB_SUB_CHANNEL = "activeTokenChannel";

    String WAITING_TOKEN_PUB_SUB_CHANNEL = "waitingTokenChannel";
    String WAITING_QUEUE_STATUS_PUB_SUB_CHANNEL = "waitingQueueStatusChannel";
    String ACTIVE_TOKEN_PROCESSING_LOCK_KEY = "activeTokenProcessingLock";
    String WAITING_TOKEN_PROCESSING_LOCK_KEY = "waitingTokenProcessingLock";

    String WAITING_QUEUE_STATUS = "waitingQueueStatus";

    String WAITING_QUEUE_STATUS_LOCK_KEY = "waitingQueueStatusLock";
    String TOKEN_SESSION_ID_MAP = "tokenSessionId";

    String HEARTBEAT_HASH_KEY = "userHeartbeat";
}
