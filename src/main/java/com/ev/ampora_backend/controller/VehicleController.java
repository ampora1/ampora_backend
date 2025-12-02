package com.ev.ampora_backend.controller;

import com.ev.ampora_backend.dto.VehicleDto;
import com.ev.ampora_backend.service.VehicleService;

import com.ev.ampora_backend.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private VehicleService vehicleService;
//Build Add Employee REST API
    @PostMapping

    public ResponseEntity<VehicleDto> createVehicle(@RequestBody VehicleDto vehicleDto) {
   VehicleDto savedVehicle= vehicleService.createVehicle(vehicleDto);

    return  new ResponseEntity<>(savedVehicle,HttpStatus.CREATED);

    }
    //Build Get Vehicle REST API
    @GetMapping("{id}")
    public  ResponseEntity<VehicleDto> getVehicleById(@PathVariable("id") String vehicleId) {

        VehicleDto vehicleDto=vehicleService.getVehicleById(vehicleId);
        return ResponseEntity.ok(vehicleDto);
    }
    //Build Get all Vehicles REST API
    @GetMapping
    public  ResponseEntity<List<VehicleDto>> getAllVehicles(){
        List<VehicleDto> vehicles=vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }
    //Build updateVehicle REST API
    @PutMapping("{id}")
    public  ResponseEntity<VehicleDto> updatedVehicle(@PathVariable("id") String vehicleId, @RequestBody VehicleDto updatedvehicle){
      VehicleDto vehicleDto=  vehicleService.updateVehicle(vehicleId,updatedvehicle);
      return ResponseEntity.ok(vehicleDto);
    }
    //Build Delete Vehicle REST API
    @DeleteMapping("{id}")
    public ResponseEntity<String>deleteVehicle(@PathVariable("id") String vehicleId){
        return ResponseEntity.ok("Vehicle deleted Successfully!");
    }
}
