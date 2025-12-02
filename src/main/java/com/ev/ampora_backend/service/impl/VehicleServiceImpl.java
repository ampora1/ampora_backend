package com.ev.ampora_backend.service.impl;

import com.ev.ampora_backend.dto.VehicleDto;
import com.ev.ampora_backend.entity.Vehicle;
import com.ev.ampora_backend.exception.ResourceNotFoundException;
import com.ev.ampora_backend.mapper.VehicleMapper;
import com.ev.ampora_backend.repository.VehicleRepository;
import com.ev.ampora_backend.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    private VehicleRepository vehicleRepository;
    @Override
    public VehicleDto createVehicle(VehicleDto vehicleDto) {
        Vehicle vehicle = VehicleMapper.mapToVehicle(vehicleDto);
        Vehicle savedVehicle=vehicleRepository.save(vehicle);
        return VehicleMapper.mapToVehicleDto(savedVehicle);
    }

    @Override
    public VehicleDto getVehicleById(String vehicleId) {
    Vehicle vehicle=vehicleRepository.findById(vehicleId)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle does not exists with the given ID :"+vehicleId));

        return  VehicleMapper.mapToVehicleDto(vehicle);

    }

    @Override
    public List<VehicleDto> getAllVehicles(

    ){
        List<Vehicle> vehicles=vehicleRepository.findAll();
        return  vehicles.stream().map((vehicle)  ->VehicleMapper.mapToVehicleDto(vehicle))
                .collect(Collectors.toList());
    }

    @Override
    public VehicleDto updateVehicle(String vehicleId, VehicleDto updatedVehicle) {
       Vehicle vehicle= vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("Vehicle does not exist with the given ID :"+vehicleId));


        vehicle.setModelName(updatedVehicle.getModelName());
        vehicle.setBrand(updatedVehicle.getBrand());
        vehicle.setBatteryCapacityKwh(updatedVehicle.getBatteryCapacityKwh());
        vehicle.setEfficiencyKmPerKwh(updatedVehicle.getEfficiencyKmPerKwh());
        vehicle.setConnectorType(updatedVehicle.getConnectorType());
        Vehicle updatedVehicleObj=vehicleRepository.save(vehicle);
    return VehicleMapper.mapToVehicleDto(updatedVehicleObj);
    }

    @Override
    public void deleteVehicle(String vehicleId) {
        Vehicle vehicle=vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle does not exist with the given ID :"+vehicleId));

      vehicleRepository.deleteById(vehicleId);

    }
}
