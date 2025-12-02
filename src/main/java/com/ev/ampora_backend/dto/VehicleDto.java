package com.ev.ampora_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {

    private Long vehicleId;
    private String modelName;
    private String brand;
    private double batteryCapacityKwh;
    private double efficiencyKmPerKwh;
    private String connectorType; // CCS2 / CHAdeMO / Type2

}
