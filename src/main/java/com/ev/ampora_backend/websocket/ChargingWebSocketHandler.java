package com.ev.ampora_backend.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ChargingWebSocketHandler extends TextWebSocketHandler {

    private final ChargingSessionManager sessionManager;

    public ChargingWebSocketHandler(ChargingSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessionManager.add(session);
        System.out.println("🟢 WS CONNECTED: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("📥 FROM ESP32: " + message.getPayload());

        // 🔥 BROADCAST TO ALL CONNECTED CLIENTS
        for (WebSocketSession client : sessionManager.getSessions()) {
            if (client.isOpen()) {
                client.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionManager.remove(session);
        System.out.println("🔴 WS CLOSED: " + session.getId());
    }
}
