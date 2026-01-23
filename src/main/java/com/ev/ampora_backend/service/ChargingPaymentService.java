package com.ev.ampora_backend.service;

import com.ev.ampora_backend.dto.ChargingPaymentRequestDTO;
import com.ev.ampora_backend.entity.ChargingPayment;
import com.ev.ampora_backend.entity.PaymentStatus;
import com.ev.ampora_backend.repository.ChargingPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChargingPaymentService {

    private final ChargingPaymentRepository repository;

    /* CREATE (PENDING) */
    public String createPending(ChargingPaymentRequestDTO dto) {

        ChargingPayment payment = ChargingPayment.builder()
                .sessionId(dto.getSessionId())
                .userId(dto.getUserId())
                .energyUsed(dto.getEnergy())
                .amount(dto.getAmount())
                .status(PaymentStatus.PENDING)
                .build();

        repository.save(payment);
        return payment.getId();
    }

    /* READ (BY ID) */
    public ChargingPayment getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Charging payment not found"));
    }

    /* READ (BY USER) */
    public List<ChargingPayment> getByUser(String userId) {
        return repository.findByUserId(userId);
    }

    /* UPDATE (CONFIRM PAYMENT) */
    public void confirmPayment(String id, String payHerePaymentId) {

        ChargingPayment payment = getById(id);

        if (payment.getStatus() == PaymentStatus.SUCCESS) return;

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentId(payHerePaymentId);

        repository.save(payment);
    }

    /* UPDATE (CANCEL) */
    public void cancelPayment(String id) {

        ChargingPayment payment = getById(id);

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new RuntimeException("Cannot cancel confirmed payment");
        }

        payment.setStatus(PaymentStatus.FAILED);
        repository.save(payment);
    }

    /* DELETE */
    public void delete(String id) {
        repository.deleteById(id);
    }
}
