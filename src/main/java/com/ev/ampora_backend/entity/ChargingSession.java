package com.ev.ampora_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "charging_session")
@Getter
@Setter
public class ChargingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double energyKwh;
    private Double amountLkr;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String status; // ONGOING, COMPLETED, PAID
}
