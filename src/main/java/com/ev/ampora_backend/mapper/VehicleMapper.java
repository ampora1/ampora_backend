package com.ev.ampora_backend.mapper;

import com.ev.ampora_backend.dto.VehicleDto;
import com.ev.ampora_backend.entity.Vehicle;

public class VehicleMapper {

    // Convert Entity → DTO
    public static VehicleDto mapToVehicleDto(Vehicle vehicle) {
        return new VehicleDto(
                vehicle.getVehicleId(),
                vehicle.getModelName(),
                vehicle.getBrand(),
                vehicle.getBatteryCapacityKwh(),
                vehicle.getEfficiencyKmPerKwh(),
                vehicle.getConnectorType()
        );
    }

    // Convert DTO → Entity
    public static Vehicle mapToVehicle(VehicleDto vehicleDto) {
        return new Vehicle(
                vehicleDto.getVehicleId(),
                vehicleDto.getModelName(),
                vehicleDto.getBrand(),
                vehicleDto.getBatteryCapacityKwh(),
                vehicleDto.getEfficiencyKmPerKwh(),
                vehicleDto.getConnectorType()
                // User is NOT set here → handled in service layer
        );
    }
}
