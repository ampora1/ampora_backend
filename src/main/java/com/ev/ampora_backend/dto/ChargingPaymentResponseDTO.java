package com.ev.ampora_backend.dto;

import com.ev.ampora_backend.entity.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChargingPaymentResponseDTO {

    private String id;
    private String sessionId;
    private double energyUsed;
    private double amount;
    private PaymentStatus status;
    private String paymentId;
    private LocalDateTime createdAt;
}

