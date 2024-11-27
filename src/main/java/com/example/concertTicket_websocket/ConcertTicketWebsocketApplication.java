package com.example.concertTicket_websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ConcertTicketWebsocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcertTicketWebsocketApplication.class, args);
	}

}
