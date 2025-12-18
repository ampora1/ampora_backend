package com.ev.ampora_backend.dto;

import lombok.Data;

public class PaymentRequestDTO {
    @Data
    public class PaymentRequestDTO {
        private String orderId;
        private double amount;
        private String currency;
        private String customerName;
        private String customerEmail;
        private String customerPhone;
    }
}
