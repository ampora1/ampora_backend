package com.ev.ampora_backend.controller;

import com.ev.ampora_backend.dto.BookingDTO;
import com.ev.ampora_backend.dto.SubscriptionDto;
import com.ev.ampora_backend.dto.payment.PayHereHashRequest;
import com.ev.ampora_backend.dto.payment.PayHereHashResponse;
import com.ev.ampora_backend.entity.Booking;
import com.ev.ampora_backend.entity.BookingStatus;
import com.ev.ampora_backend.entity.ChargingSession;
import com.ev.ampora_backend.entity.PaymentStatus;
import com.ev.ampora_backend.repository.BookingRepository;
import com.ev.ampora_backend.repository.ChargingSessionRepository;
import com.ev.ampora_backend.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    private final PayHereService payHereService;
    private final SubscriptionService subscriptionService;
    private final BookingService bookingService;
    private  final BookingRepository bookingRepository;
    private final ChargingSessionRepository chargingSessionRepository;
    private final ChargingPaymentService chargingPaymentService;

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

    @PostMapping("/pending")
    public ResponseEntity<Map<String, String>> createPendingBooking(
            @RequestBody BookingDTO dto) {

        String bookingId = bookingService.createPendingBooking(dto);

        return ResponseEntity.ok(
                Map.of("bookingId", bookingId)
        );
    }
    @PostMapping("/payhere/notify1")
    public ResponseEntity<String> payHereNotify(
            @RequestParam Map<String, String> params) {

        System.out.println("🔔 PayHere Notify Received");
        params.forEach((k, v) -> System.out.println(k + " = " + v));

        if (!"2".equals(params.get("status_code"))) {
            return ResponseEntity.ok("Ignored");
        }

        String bookingId = params.get("custom_1");
        String paymentId = params.get("payment_id");

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow();

        if (booking.getPaymentStatus() == PaymentStatus.SUCCESS) {
            return ResponseEntity.ok("Already processed");
        }

        booking.setPaymentStatus(PaymentStatus.SUCCESS);
        booking.setBookingStatus(BookingStatus.PENDING);
//        booking.setPaymentId(paymentId);
        bookingRepository.save(booking);

        System.out.println("✅ Booking confirmed");

        return ResponseEntity.ok("OK");
    }

    @PostMapping("/payhere/charging-notify")
    public ResponseEntity<String> chargingNotify(
            @RequestParam Map<String, String> params) {

        if (!"2".equals(params.get("status_code"))) {
            return ResponseEntity.ok("Ignored");
        }

        String chargingPaymentId = params.get("custom_1");
        String payHerePaymentId = params.get("payment_id");

        chargingPaymentService.confirmPayment(
                chargingPaymentId,
                payHerePaymentId
        );

        return ResponseEntity.ok("OK");
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
