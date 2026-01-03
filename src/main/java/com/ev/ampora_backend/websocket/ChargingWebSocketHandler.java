package com.ev.ampora_backend.websocket;

import com.ev.ampora_backend.dto.RFIDResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ev.ampora_backend.service.RFIDService;
import com.ev.ampora_backend.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Optional;

@Component
public class ChargingWebSocketHandler extends TextWebSocketHandler {

    private final ChargingSessionManager sessionManager;
    private final RFIDService rfidService;
    private final ObjectMapper mapper = new ObjectMapper();

    public ChargingWebSocketHandler(
            ChargingSessionManager sessionManager,
            RFIDService rfidService
    ) {
        this.sessionManager = sessionManager;
        this.rfidService = rfidService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessionManager.add(session);
        System.out.println("🟢 WS CONNECTED: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        System.out.println("📥 FROM ESP32: " + message.getPayload());

        JsonNode root = mapper.readTree(message.getPayload());
        String type = root.get("type").asText();

        switch (type) {

            /* ================= AUTH ================= */
            case "AUTH_REQUEST" -> handleAuth(session, root);

            /* ================= LIVE DATA ================= */
            case "LIVE" -> broadcastLiveData(message);

            /* ================= SESSION END ================= */
            case "SESSION_END" -> handleSessionEnd(root);

            default -> System.out.println("⚠️ Unknown message type");
        }
    }

    private void handleAuth(WebSocketSession session, JsonNode root) throws Exception {

        String uid = root.get("uid").asText();
        System.out.println("🔐 AUTH REQUEST UID: " + uid);

        RFIDResponse userOpt = rfidService.getUserRFID(uid);

        String response;




            response = """
            {
              "type": "AUTH_RESPONSE",
              "authorized": true,
              "name": "%s"
            }
            """.formatted(userOpt.getUsername());

            System.out.println("✅ AUTH OK: " + userOpt.getUsername());



        session.sendMessage(new TextMessage(response));
    }

    private void broadcastLiveData(TextMessage message) throws Exception {
        for (WebSocketSession client : sessionManager.getSessions()) {
            if (client.isOpen()) {
                client.sendMessage(message);
            }
        }
    }

    private void handleSessionEnd(JsonNode root) {
        System.out.println("🧾 SESSION END: " + root);
        // optional: persist billing data
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionManager.remove(session);
        System.out.println("🔴 WS CLOSED: " + session.getId());
    }
}
