package com.ev.ampora_backend.websocket;

import com.ev.ampora_backend.dto.RFIDResponse;
import com.ev.ampora_backend.service.RFIDService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

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

    /* ================= CONNECT ================= */

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        String query = session.getUri().getQuery();
        String userId = extractUserId(query);

        if (userId != null) {
            sessionManager.addUserSession(userId, session);
            System.out.println("Frontend connected: " + userId);
        } else {
            System.out.println("Charger connected: " + session.getId());
        }
    }

    /* ================= MESSAGE ================= */

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        System.out.println("WS IN: " + message.getPayload());

        JsonNode root = mapper.readTree(message.getPayload());
        String type = root.get("type").asText();

        switch (type) {

            case "AUTH_REQUEST" -> handleAuth(session, root);

            case "LIVE" -> handleLive(session, message);

            case "SESSION_END" -> handleSessionEnd(session, message);

            default -> System.out.println("Unknown WS type: " + type);
        }
    }

    /* ================= AUTH ================= */

    private void handleAuth(WebSocketSession session, JsonNode root) throws Exception {

        String uid = root.get("uid").asText();
        System.out.println("AUTH REQUEST UID: " + uid);

        RFIDResponse user = rfidService.getUserRFID(uid);

        // RFID not found
        if (user == null) {

            String response = """
            {
              "type": "AUTH_RESPONSE",
              "authorized": false
            }
            """;

            session.sendMessage(new TextMessage(response));
            return;
        }

        // Bind charger to user
        String chargerId = session.getId();
        sessionManager.setActiveUser(chargerId, user.getUserId());

        String response = """
        {
          "type": "AUTH_RESPONSE",
          "authorized": true,
          "name": "%s"
        }
        """.formatted(user.getUsername());

        session.sendMessage(new TextMessage(response));

        System.out.println("AUTH OK: " + user.getUsername());
    }

    /* ================= LIVE DATA ================= */

    private void handleLive(WebSocketSession chargerSession, TextMessage message) throws Exception {

        String chargerId = chargerSession.getId();
        String userId = sessionManager.getActiveUser(chargerId);

        if (userId == null) {
            System.out.println("No active user for charger");
            return;
        }

        WebSocketSession userSession = sessionManager.getUserSession(userId);

        if (userSession != null && userSession.isOpen()) {
            userSession.sendMessage(message);
        } else {
            System.out.println("User not connected yet: " + userId);
        }
    }


    /* ================= SESSION END ================= */

    private void handleSessionEnd(WebSocketSession chargerSession, TextMessage message) throws Exception {

        String chargerId = chargerSession.getId();
        String userId = sessionManager.getActiveUser(chargerId);

        if (userId == null) return;

        WebSocketSession userSession = sessionManager.getUserSession(userId);

        if (userSession != null && userSession.isOpen()) {
            userSession.sendMessage(message);
        }

        sessionManager.removeActiveUser(chargerId);
    }

    /* ================= DISCONNECT ================= */

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

        sessionManager.removeSession(session);
        sessionManager.removeActiveUser(session.getId());

        System.out.println("WS CLOSED: " + session.getId());
    }

    /* ================= HELPER ================= */

    private String extractUserId(String query) {
        if (query == null) return null;

        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair[0].equals("userId")) {
                return pair[1];
            }
        }
        return null;
    }
}
