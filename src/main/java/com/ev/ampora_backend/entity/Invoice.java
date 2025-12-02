package com.ev.ampora_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String invoiceId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate month;
    private double totalKwh;
    private double amount;
    private boolean paid;
}
