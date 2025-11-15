package com.ev.ampora_backend.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;  // <-- KEEP AS STRING

    private String fullName;
    private String email;
    private String phone;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // One user → One vehicle
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Vehicle vehicle;

    // Subscription
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Subscription subscription;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private RFIDCard rfidCard;
}
