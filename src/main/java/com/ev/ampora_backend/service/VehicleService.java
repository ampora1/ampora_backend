package com.ev.ampora_backend.service;

import com.ev.ampora_backend.dto.VehicleDto;
import com.ev.ampora_backend.entity.Vehicle;
import java.util.List;


public interface VehicleService {
    VehicleDto createVehicle(VehicleDto vehicleDto);
    VehicleDto getVehicleById(String vehicleId);
    List<VehicleDto> getAllVehicles();
    VehicleDto updateVehicle(String vehicleId, VehicleDto updatedVehicleDto);

    void deleteVehicle(String vehicleId);

}
