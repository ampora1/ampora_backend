package com.ev.ampora_backend.repository;

import com.ev.ampora_backend.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepository extends JpaRepository<Station,String> {
    List<Station> findByOperator_UserId(String operatorId);
}
