package com.ev.ampora_backend.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class VehicleDTO {
    private String vehicleId;
    private String model;
    private double batteryCapacityKwh;
    private double efficiencyKmPerKwh;
    private String connectorType;
    private String userId;
}
