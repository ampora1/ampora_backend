package com.ev.ampora_backend.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class ChargingSessionManager {

    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final Map<String, String> chargerActiveUser = new ConcurrentHashMap<>();

    // FRONTEND SESSION
    public void addUserSession(String userId, WebSocketSession session) {
        userSessions.put(userId, session);
    }

    public WebSocketSession getUserSession(String userId) {
        return userSessions.get(userId);
    }

    public void removeUserSession(String userId) {
        userSessions.remove(userId);
    }

    // CHARGER SESSION
    public void setActiveUser(String chargerId, String userId) {
        chargerActiveUser.put(chargerId, userId);
    }

    public String getActiveUser(String chargerId) {
        return chargerActiveUser.get(chargerId);
    }

    public void removeActiveUser(String chargerId) {
        chargerActiveUser.remove(chargerId);
    }
}
