package com.ev.ampora_backend.repository;

import com.ev.ampora_backend.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle,String> { }
