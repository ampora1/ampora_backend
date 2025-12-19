package com.ev.ampora_backend.controller;

import com.ev.ampora_backend.dto.payment.PayHereHashRequest;
import com.ev.ampora_backend.dto.payment.PayHereHashResponse;
import com.ev.ampora_backend.service.PayHereService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    private final PayHereService payHereService;

    /* ============================================================
       1️⃣ HASH GENERATION ENDPOINT
       Called by frontend before redirecting to PayHere
    ============================================================ */
    @PostMapping("/payhere/hash")
    public PayHereHashResponse generateHash(@RequestBody PayHereHashRequest req) {
        String hash = payHereService.generateHash(
                req.getOrderId(),
                req.getAmount(),
                req.getCurrency()
        );

        return new PayHereHashResponse(
                payHereService.getMerchantId(),
                hash
        );
    }

    /* ============================================================
       2️⃣ PAYHERE NOTIFY URL (SERVER → SERVER)
       PayHere calls this after payment
    ============================================================ */
    @PostMapping("/payhere/notify")
    public String payHereNotify(HttpServletRequest request) {

        String merchantId = request.getParameter("merchant_id");
        String orderId = request.getParameter("order_id");
        String amount = request.getParameter("payhere_amount");
        String currency = request.getParameter("payhere_currency");
        String statusCode = request.getParameter("status_code");
        String receivedHash = request.getParameter("md5sig");

        boolean valid = payHereService.verifyNotifyHash(
                merchantId,
                orderId,
                amount,
                currency,
                statusCode,
                receivedHash
        );

        if (!valid) {
            System.out.println("⚠️ PayHere hash verification FAILED");
            return "INVALID HASH";
        }

        /*
         status_code meanings:
         2 = Success
         0 = Pending
         -1 = Cancelled
         -2 = Failed
         -3 = Charged Back
        */

        switch (statusCode) {
            case "2" -> {
                System.out.println("✅ PAYMENT SUCCESS: " + orderId);
                // TODO: update booking/payment status = PAID
            }
            case "-1" -> System.out.println("❌ PAYMENT CANCELLED: " + orderId);
            case "-2" -> System.out.println("❌ PAYMENT FAILED: " + orderId);
            case "-3" -> System.out.println("⚠️ PAYMENT CHARGEBACK: " + orderId);
            default -> System.out.println("ℹ️ PAYMENT STATUS: " + statusCode);
        }

        return "OK";
    }
}
