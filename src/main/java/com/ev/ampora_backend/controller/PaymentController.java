package com.ev.ampora_backend.controller;

import com.ev.ampora_backend.dto.payment.PayHereHashRequest;
import com.ev.ampora_backend.dto.payment.PayHereHashResponse;
import com.ev.ampora_backend.service.PayHereService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    private final PayHereService payHereService;

    @PostMapping("/payhere/hash")
    public PayHereHashResponse generateHash(@RequestBody PayHereHashRequest req) {

        // Always normalize amount here
        BigDecimal amt = new BigDecimal(req.getAmount())
                .setScale(2, RoundingMode.HALF_UP);

        String formattedAmount = amt.toPlainString();

        String hash = payHereService.generateHash(
                req.getOrderId(),
                formattedAmount,
                req.getCurrency()
        );

        System.out.println(payHereService.getMerchantId()+req.getOrderId()+formattedAmount+req.getCurrency()+hash);
        return new PayHereHashResponse(
                payHereService.getMerchantId(),
                req.getOrderId(),
                formattedAmount,
                req.getCurrency(),
                hash
        );
    }
}
