package com.example.concertTicket_websocket.websocket.utils;

import com.example.concertTicket_websocket.websocket.dto.WaitingDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MessageParser {

    public List<String> parseActiveTokenMessage(String message) {
        // message가 다음과 같은 형식으로 옴
        // {"abcd", "dkek", "ekek"}
        // 따라서 {}를 제거해주기 위해서 substring을 사용함
        // 또한 {}를 제거한 후, 각 문자열을 split한 후에 공백을 제거하기 위해서 trim을 사용함
        message = message.substring(1, message.length()-1);
        return List.of(message.split(",")).stream()
                                                .map(String::trim)
                                                .toList();
    }

    public List<WaitingDTO> parseWaitingTokenMessage(String message) {
        // message가 다음과 같은 형식으로 옴
        // {"abcd", "dkek", "ekek"}
        // 따라서 {}를 제거해주기 위해서 substring을 사용함
        // 또한 {}를 제거한 후, 각 문자열을 split한 후에 공백을 제거하기 위해서 trim을 사용함
        message = message.substring(1, message.length()-1);
        log.info("message?, {}", message);
        return new ArrayList<>();
    }
}
