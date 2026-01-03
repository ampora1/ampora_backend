package com.ev.ampora_backend.controller;

import com.ev.ampora_backend.dto.SubscriptionDto;
import com.ev.ampora_backend.dto.payment.PayHereHashRequest;
import com.ev.ampora_backend.dto.payment.PayHereHashResponse;
import com.ev.ampora_backend.service.PayHereService;
import com.ev.ampora_backend.service.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    private final PayHereService payHereService;
    private final SubscriptionService subscriptionService;

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

    @PostMapping("payhere/notify")
    public ResponseEntity<String> payHereNotify(@RequestBody SubscriptionDto request) {


            System.out.println("Payment Success");


        System.out.println(request.getSubscriptionId()+request.getUserId()+request.getPlanName());

            SubscriptionDto dto = SubscriptionDto.builder()
                    .subscriptionId(request.getSubscriptionId())
                    .userId(request.getUserId())
                    .planName(request.getPlanName())
                    .active(true)
                    .build();

            subscriptionService.createSubscription(dto);

        return ResponseEntity.ok("OK");
    }
    private double getMonthlyKwh(String planId) {
        return switch (planId) {
            case "basic" -> 100;
            case "premium" -> 500;
            case "enterprise" -> 1000;
            default -> 0;
        };
    }


}
