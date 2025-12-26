package com.ev.ampora_backend.service;

import com.ev.ampora_backend.entity.ChargingSession;
import com.ev.ampora_backend.repository.ChargingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChargingSessionService {

    private final ChargingSessionRepository repo;

    private static final double RATE_PER_KWH = 85.0;

    public ChargingSession startSession() {
        ChargingSession session = new ChargingSession();
        session.setStartTime(LocalDateTime.now());
        session.setStatus("ONGOING");
        return repo.save(session);
    }

    public ChargingSession updateLive(double energy) {
        ChargingSession session = repo
                .findFirstByStatus("ONGOING")
                .orElseGet(this::startSession);

        session.setEnergyKwh(energy);
        session.setAmountLkr(energy * RATE_PER_KWH);
        return repo.save(session);
    }

    public ChargingSession endSession(double energy) {
        ChargingSession session =
                repo.findFirstByStatus("ONGOING")
                        .orElseThrow();

        session.setEnergyKwh(energy);
        session.setAmountLkr(energy * RATE_PER_KWH);
        session.setEndTime(LocalDateTime.now());
        session.setStatus("COMPLETED");

        return repo.save(session);
    }
}
