package com.ev.ampora_backend.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChargingSessionManager {

    /* ================= USER SESSIONS ================= */
    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    public void addUserSession(String userId, WebSocketSession session) {
        userSessions.put(userId, session);
    }

    public WebSocketSession getUserSession(String userId) {
        return userSessions.get(userId);
    }

    /* ================= OPERATOR SESSIONS ================= */
    private final Set<WebSocketSession> operatorSessions = ConcurrentHashMap.newKeySet();

    public void addOperatorSession(WebSocketSession session) {
        operatorSessions.add(session);
    }

    public Set<WebSocketSession> getOperatorSessions() {
        return operatorSessions;
    }

    /* ================= CHARGER ACTIVE USER ================= */
    private final Map<String, String> chargerActiveUser = new ConcurrentHashMap<>();

    public void setActiveUser(String chargerId, String userId) {
        chargerActiveUser.put(chargerId, userId);
    }

    public String getActiveUser(String chargerId) {
        return chargerActiveUser.get(chargerId);
    }

    public void removeActiveUser(String chargerId) {
        chargerActiveUser.remove(chargerId);
    }

    /* ================= CLEANUP ================= */
    public void removeSession(WebSocketSession session) {
        userSessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
        operatorSessions.remove(session);
        chargerActiveUser.remove(session.getId());
    }
}
