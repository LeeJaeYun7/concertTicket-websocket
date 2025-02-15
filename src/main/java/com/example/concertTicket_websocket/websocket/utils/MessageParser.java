package com.example.concertTicket_websocket.websocket.utils;

import java.util.List;

public class MessageParser {

    public List<String> parseMessage(String message) {
        // message가 다음과 같은 형식으로 옴
        // {"abcd", "dkek", "ekek"}
        // 따라서 {}를 제거해주기 위해서 substring을 사용함
        // 또한 {}를 제거한 후, 각 문자열을 split한 후에 공백을 제거하기 위해서 trim을 사용함
        message = message.substring(1, message.length()-1);
        return List.of(message.split(",")).stream()
                                                .map(String::trim)
                                                .toList();
    }
}
