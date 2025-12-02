package com.ev.ampora_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="vehiclee")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Vehicle_id")

    private Long vehicleId;
    private String modelName;
    private String brand;
    private double batteryCapacityKwh;
    private double efficiencyKmPerKwh;
    private String connectorType; // CCS2 / CHAdeMO / Type2

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    // 👇👇👇 Add this manually
    public Vehicle(Long vehicleId,
                   String modelName,
                   String brand,
                   double batteryCapacityKwh,
                   double efficiencyKmPerKwh,
                   String connectorType) {

        this.vehicleId = vehicleId;
        this.modelName = modelName;
        this.brand = brand;
        this.batteryCapacityKwh = batteryCapacityKwh;
        this.efficiencyKmPerKwh = efficiencyKmPerKwh;
        this.connectorType = connectorType;
    }
}


