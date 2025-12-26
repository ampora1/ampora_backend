package com.ev.ampora_backend.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class PayHereService {

    @Getter
    @Value("${payhere.merchant.id}")
    private String merchantId;

    @Value("${payhere.merchant.secret}")
    private String merchantSecret;

    /* ------------------------------------------------
       Generate PayHere HASH
    ------------------------------------------------ */
    public String generateHash(String orderId, String amount, String currency) {
        try {
            String secretHash = md5(merchantSecret).toUpperCase();
            String raw = merchantId + orderId + amount + currency + secretHash;
            return md5(raw).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Hash generation failed", e);
        }
    }

    /* ------------------------------------------------
       Verify PayHere Notify Hash
    ------------------------------------------------ */
    public boolean verifyNotifyHash(
            String merchantId,
            String orderId,
            String payhereAmount,
            String payhereCurrency,
            String statusCode,
            String receivedHash
    ) {
        String secretHash = md5(merchantSecret).toUpperCase();
        String raw = merchantId + orderId + payhereAmount + payhereCurrency + statusCode + secretHash;
        String calculated = md5(raw).toUpperCase();
        return calculated.equals(receivedHash);
    }

    /* ------------------------------------------------
       MD5 Utility
    ------------------------------------------------ */
    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
