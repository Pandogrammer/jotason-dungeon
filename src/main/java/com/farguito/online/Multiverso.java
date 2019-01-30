package com.farguito.online;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Scope("session")
public class Multiverso extends TextWebSocketHandler {
	
	@Autowired
	Partida partida;
	
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		try { 
			session.sendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(message.getPayload());
		}
	}
	
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String sessionId = session.getId();
		System.out.println(sessionId);
		partida.init("Caca");
	}

	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String sessionId = session.getId();
		System.out.println(sessionId);
	}
}
