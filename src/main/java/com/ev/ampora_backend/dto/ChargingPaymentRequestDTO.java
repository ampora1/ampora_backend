package com.ev.ampora_backend.dto;

import lombok.Data;

@Data
public class ChargingPaymentRequestDTO {
    private String sessionId;
    private String userId;
    private double energy;
    private double amount;
}
