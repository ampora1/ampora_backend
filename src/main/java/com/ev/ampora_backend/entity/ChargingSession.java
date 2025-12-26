package com.ev.ampora_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "charging_sessions")
public class ChargingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String sessionId;

    private Double energyKwh;
    private Double billLkr;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Boolean paid = false;

    public ChargingSession() {}

    public ChargingSession(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    // getters & setters

    public String getSessionId() { return sessionId; }

    public Double getEnergyKwh() { return energyKwh; }
    public void setEnergyKwh(Double energyKwh) { this.energyKwh = energyKwh; }

    public Double getBillLkr() { return billLkr; }
    public void setBillLkr(Double billLkr) { this.billLkr = billLkr; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public Boolean getPaid() { return paid; }
    public void setPaid(Boolean paid) { this.paid = paid; }
}
