package com.ev.ampora_backend.service;

import com.ev.ampora_backend.entity.ChargingSession;
import com.ev.ampora_backend.repository.ChargingSessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChargingSessionService {

    private final ChargingSessionRepository repo;
    private ChargingSession activeSession;

    public ChargingSessionService(ChargingSessionRepository repo) {
        this.repo = repo;
    }

    // Called when charging starts
    public void startSession() {
        activeSession = new ChargingSession(LocalDateTime.now());
        repo.save(activeSession);
        System.out.println("⚡ Charging session started");
    }

    // Called continuously (LIVE data)
    public void updateEnergy(double energy) {
        if (activeSession == null) return;
        activeSession.setEnergyKwh(energy);
        repo.save(activeSession);
    }

    // Called once when charging ends
    public ChargingSession endSession(double energy, double bill) {
        if (activeSession == null) return null;

        activeSession.setEnergyKwh(energy);
        activeSession.setBillLkr(bill);
        activeSession.setEndTime(LocalDateTime.now());

        ChargingSession finished = repo.save(activeSession);
        activeSession = null;

        System.out.println("✅ Charging session saved");
        return finished;
    }

    public ChargingSession getLastSession() {
        return repo.findAll()
                .stream()
                .reduce((first, second) -> second)
                .orElse(null);
    }
}
