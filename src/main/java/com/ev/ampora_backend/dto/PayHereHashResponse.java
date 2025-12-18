package com.ev.ampora_backend.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PayHereHashResponse {
    private String merchantId;
    private String hash;
}
