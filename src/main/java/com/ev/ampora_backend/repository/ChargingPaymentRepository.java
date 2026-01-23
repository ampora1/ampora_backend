package com.ev.ampora_backend.repository;

import com.ev.ampora_backend.entity.ChargingPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChargingPaymentRepository
        extends JpaRepository<ChargingPayment, String> {

    List<ChargingPayment> findByUserId(String userId);

    Optional<ChargingPayment> findBySessionId(String sessionId);
}

