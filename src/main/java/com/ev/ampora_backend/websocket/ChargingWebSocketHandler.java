package com.ev.ampora_backend.websocket;

import com.ev.ampora_backend.service.ChargingSessionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class ChargingWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper = new ObjectMapper();
    private final ChargingSessionService service;
    private final WebSocketBroadcaster broadcaster;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        broadcaster.add(session);
    }

    @Override
    protected void handleTextMessage(
            WebSocketSession session, TextMessage message) throws Exception {

        JsonNode json = mapper.readTree(message.getPayload());
        String type = json.has("type") ? json.get("type").asText() : "LIVE";

        if ("LIVE".equals(type)) {
            service.updateLive(json.get("energy").asDouble());
        }

        if ("SESSION_END".equals(type)) {
            service.endSession(json.get("energy").asDouble());
        }

        broadcaster.broadcast(message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        broadcaster.remove(session);
    }
}
