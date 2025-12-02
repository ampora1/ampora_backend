package com.ev.ampora_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class RFIDCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cardId;

    private String rfidNumber; // Value read from ESP32 reader

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
