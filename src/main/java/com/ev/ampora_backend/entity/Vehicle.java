package com.ev.ampora_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String vehicleId;

    private String model;

    private double batteryCapacityKwh;
    private double efficiencyKmPerKwh;
    private String connectorType; // CCS2 / CHAdeMO / Type2

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
