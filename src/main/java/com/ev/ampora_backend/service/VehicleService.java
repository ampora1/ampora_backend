package com.ev.ampora_backend.service;

import com.ev.ampora_backend.dto.StationRequestDTO;
import com.ev.ampora_backend.dto.StationResponseDTO;
import com.ev.ampora_backend.dto.VehicleDTO;
import com.ev.ampora_backend.entity.Station;
import com.ev.ampora_backend.entity.User;
import com.ev.ampora_backend.entity.Vehicle;
import com.ev.ampora_backend.repository.UserRepository;
import com.ev.ampora_backend.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class VehicleService {
  private final VehicleRepository vehicleRepo;
  private final UserRepository userRepo;

  public VehicleDTO saveVehicle(VehicleDTO dto){
      User user = userRepo.findById(dto.getUserId()).orElseThrow(()->new RuntimeException("User not found"));
      Vehicle vehicle=Vehicle.builder().model(dto.getModel()).batteryCapacityKwh(dto.getBatteryCapacityKwh()).efficiencyKmPerKwh(dto.getEfficiencyKmPerKwh()).connectorType(dto.getConnectorType()).user(user).build();
      Vehicle saved= vehicleRepo.save(vehicle);
      dto.setVehicleId(saved.getVehicleId());
      return dto;

  }

  public List<VehicleDTO> getAllVehicle(){
      return vehicleRepo.findAll().stream().map(v ->VehicleDTO.builder().vehicleId(v.getVehicleId()).model(v.getModel()).batteryCapacityKwh(v.getBatteryCapacityKwh()).efficiencyKmPerKwh(v.getEfficiencyKmPerKwh()).connectorType(v.getConnectorType()).userId(v.getUser().getUserId()).build()).toList();
  }

  public VehicleDTO getVehicleById(String id){
     Vehicle v =vehicleRepo.findById(id).orElseThrow(() -> new RuntimeException("Vehicle not find"));

     return VehicleDTO.builder().vehicleId(v.getVehicleId()).model(v.getModel()).batteryCapacityKwh(v.getBatteryCapacityKwh()).efficiencyKmPerKwh(v.getEfficiencyKmPerKwh()).connectorType(v.getConnectorType()).userId(v.getUser().getUserId()).build();
  }

  public void deleteVehicle(String id){
      System.out.println("Deleting Vehicle ID: " + id);
      if(!vehicleRepo.existsById(id)){
          throw new RuntimeException("Vehicle not found");
      }
      vehicleRepo.deleteById(id);
  }
    public List<VehicleDTO> getVehicleByuserId(String userId) {
        List<Vehicle> vehicles = vehicleRepo.findByUser_UserId(userId);

        return vehicles.stream()
                .map(v -> VehicleDTO.builder()
                        .vehicleId(v.getVehicleId())
                        .model(v.getModel())
                        .batteryCapacityKwh(v.getBatteryCapacityKwh())
                        .efficiencyKmPerKwh(v.getEfficiencyKmPerKwh())
                        .connectorType(v.getConnectorType())
                        .userId(v.getUser().getUserId())
                        .build())
                .toList();
    }


    public  VehicleDTO upDateVehicle(String id,VehicleDTO dto){
      Vehicle vehicle = vehicleRepo.findById(id).orElseThrow(()-> new RuntimeException("Vehicle not found"));
      User user =userRepo.findById(dto.getUserId()).orElseThrow(()->new RuntimeException("User not found"));
      vehicle.setModel(dto.getModel());
      vehicle.setBatteryCapacityKwh(dto.getBatteryCapacityKwh());
      vehicle.setEfficiencyKmPerKwh(dto.getEfficiencyKmPerKwh());
      vehicle.setConnectorType(dto.getConnectorType());
      vehicle.setUser(user);
      vehicleRepo.save(vehicle);
      dto.setVehicleId(id);
      return  dto;
  }

}


