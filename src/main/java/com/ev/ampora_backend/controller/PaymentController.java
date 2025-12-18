package com.ev.ampora_backend.controller;

import com.ev.ampora_backend.dto.PaymentRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin
@RequiredArgsConstructor
public class PaymentController {

    @Value("${payhere.merchant.id}")
    private String merchantId;

    @Value("${payhere.merchant.secret}")
    private String merchantSecret;

    @PostMapping("/payhere/init")
    public Map<String, String> initPayHere(@RequestBody PaymentRequestDTO req) {

        String amountFormatted = String.format("%.2f", req.getAmount());

        String hash = DigestUtils.md5DigestAsHex(
                (merchantId +
                        req.getOrderId() +
                        amountFormatted +
                        req.getCurrency() +
                        DigestUtils.md5DigestAsHex(merchantSecret.getBytes())
                ).getBytes()
        ).toUpperCase();

        Map<String, String> response = new HashMap<>();
        response.put("merchant_id", merchantId);
        response.put("order_id", req.getOrderId());
        response.put("amount", amountFormatted);
        response.put("currency", req.getCurrency());
        response.put("hash", hash);

        return response;
    }
}

