package com.ev.ampora_backend.controller;

import com.ev.ampora_backend.dto.ChargingPaymentRequestDTO;
import com.ev.ampora_backend.dto.ChargingPaymentResponseDTO;
import com.ev.ampora_backend.entity.ChargingPayment;
import com.ev.ampora_backend.service.ChargingPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/charging-payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://15.134.60.252, http://localhost:5173")
public class ChargingPaymentController {

    private final ChargingPaymentService service;

    /* CREATE (PENDING) */
    @PostMapping("/pending")
    public ResponseEntity<Map<String, String>> createPending(
            @RequestBody ChargingPaymentRequestDTO dto) {

        String id = service.createPending(dto);

        return ResponseEntity.ok(
                Map.of("chargingPaymentId", id)
        );
    }

    /* READ (BY ID) */
    @GetMapping("/{id}")
    public ChargingPaymentResponseDTO getById(@PathVariable String id) {

        ChargingPayment p = service.getById(id);

        return ChargingPaymentResponseDTO.builder()
                .id(p.getId())
                .sessionId(p.getSessionId())
                .energyUsed(p.getEnergyUsed())
                .amount(p.getAmount())
                .status(p.getStatus())
                .paymentId(p.getPaymentId())
                .createdAt(p.getCreatedAt())
                .build();
    }

    /* READ (BY USER) */
    @GetMapping("/user/{userId}")
    public List<ChargingPaymentResponseDTO> getByUser(
            @PathVariable String userId) {

        return service.getByUser(userId).stream()
                .map(p -> ChargingPaymentResponseDTO.builder()
                        .id(p.getId())
                        .sessionId(p.getSessionId())
                        .energyUsed(p.getEnergyUsed())
                        .amount(p.getAmount())
                        .status(p.getStatus())
                        .paymentId(p.getPaymentId())
                        .createdAt(p.getCreatedAt())
                        .build())
                .toList();
    }

    /* UPDATE – CONFIRM (USED BY PAYHERE NOTIFY) */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmPayment(
            @PathVariable String id,
            @RequestParam String paymentId) {

        service.confirmPayment(id, paymentId);
        return ResponseEntity.ok().build();
    }

    /* UPDATE – CANCEL */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelPayment(@PathVariable String id) {

        service.cancelPayment(id);
        return ResponseEntity.ok().build();
    }

    /* DELETE */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}


