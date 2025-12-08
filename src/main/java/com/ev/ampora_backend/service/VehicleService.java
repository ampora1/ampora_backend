package com.ev.ampora_backend.service;

import com.ev.ampora_backend.dto.StationRequestDTO;
import com.ev.ampora_backend.dto.StationResponseDTO;
import com.ev.ampora_backend.dto.VehicleDTO;
import com.ev.ampora_backend.entity.*;
import com.ev.ampora_backend.repository.BrandRepository;
import com.ev.ampora_backend.repository.ModelRepository;
import com.ev.ampora_backend.repository.UserRepository;
import com.ev.ampora_backend.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor

public class VehicleService {
    private final VehicleRepository vehicleRepo;
    private final UserRepository userRepo;
    private final BrandRepository brandRepo;
    private final ModelRepository modelRepo;

    public VehicleDTO saveVehicle(VehicleDTO dto){
        User user = userRepo.findById(dto.getUserId()).orElseThrow(()->new RuntimeException("User not found"));
        Brand brand =brandRepo.findById(dto.getBrand_id()).orElseThrow(()->new RuntimeException("Brand not found"));
        Model model = modelRepo.findById(dto.getModel_id()).orElseThrow(()->new RuntimeException("Model not found"));
        Vehicle vehicle = Vehicle.builder()
                .model(model)
                .plate(dto.getPlate())
                .rangeKm((int) dto.getRangeKm())
                .brand(brand)
                .connectorType(dto.getConnectorType())
                .user(user)
                .variant(dto.getVariant())
                .build();

        Vehicle saved= vehicleRepo.save(vehicle);
        dto.setVehicleId(saved.getVehicleId());
        return dto;

    }

    public List<VehicleDTO> getAllVehicle(){
            return vehicleRepo.findAll().stream().map(
                    v->VehicleDTO.builder()
                            .vehicleId(v.getVehicleId())
                            .model_id(v.getModel().getId())
                            .variant(v.getVariant())
                            .plate(v.getPlate())
                            .rangeKm(v.getRangeKm())
                            .connectorType(v.getConnectorType())
                            .brand_id((long) v.getBrand().getId())
                            .userId(v.getUser().getUserId())
                            .build())
                    .collect(toList());
//        return vehicleRepo.findAll().stream().map(v ->VehicleDTO.builder().vehicleId(v.getVehicleId()).model(v.getModel()).batteryCapacityKwh(v.getBatteryCapacityKwh()).efficiencyKmPerKwh(v.getEfficiencyKmPerKwh()).connectorType(v.getConnectorType()).userId(v.getUser().getUserId()).build()).toList();
    }

    public VehicleDTO getVehicleById(String id){
        Vehicle v =vehicleRepo.findById(id).orElseThrow(() -> new RuntimeException("Vehicle not find"));
            return VehicleDTO.builder()
                    .vehicleId(v.getVehicleId())
                    .model_id(v.getModel().getId())
                    .variant(v.getVariant())
                    .brand_name(v.getBrand().getName())
                    .model_name(v.getModel().getName())
                    .plate(v.getPlate())
                    .rangeKm(v.getRangeKm())
                    .connectorType(v.getConnectorType())
                    .brand_id((long) v.getBrand().getId())
                    .userId(v.getUser().getUserId())
                    .build();


//        return VehicleDTO.builder().vehicleId(v.getVehicleId()).model(v.getModel()).batteryCapacityKwh(v.getBatteryCapacityKwh()).efficiencyKmPerKwh(v.getEfficiencyKmPerKwh()).connectorType(v.getConnectorType()).userId(v.getUser().getUserId()).build();
    }
//
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
                        .model_id(v.getModel().getId())
                        .variant(v.getVariant())
                        .brand_name(v.getBrand().getName())
                        .model_name(v.getModel().getName())
                        .plate(v.getPlate())
                        .rangeKm(v.getRangeKm())
                        .connectorType(v.getConnectorType())
                        .brand_id((long) v.getBrand().getId())
                        .userId(v.getUser().getUserId())
                        .build())
                .toList();
    }
//
//
    public  VehicleDTO upDateVehicle(String id,VehicleDTO dto){
        Vehicle vehicle = vehicleRepo.findById(id).orElseThrow(()-> new RuntimeException("Vehicle not found"));
        User user =userRepo.findById(dto.getUserId()).orElseThrow(()->new RuntimeException("User not found"));
        Model model =modelRepo.findById(dto.getModel_id()).orElseThrow(()->new RuntimeException("Model not found"));
        Brand brand =brandRepo.findById(dto.getBrand_id()).orElseThrow();
        vehicle.setModel(model);
        vehicle.setPlate(dto.getPlate());
        vehicle.setRangeKm((int) dto.getRangeKm());
        vehicle.setConnectorType(dto.getConnectorType());
        vehicle.setBrand(brand);
        vehicle.setVariant(dto.getVariant());
        vehicle.setUser(user);
        vehicleRepo.save(vehicle);
        dto.setVehicleId(id);
        return  dto;
    }

}