package com.ev.ampora_backend.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String vehicleId;
    @Column(nullable = true)
    private double variant;
    @Column(nullable = true)
    private String plate;
    @Column(name = "rangeKm",nullable = true)
    private int rangeKm;
    private String connectorType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;

}
