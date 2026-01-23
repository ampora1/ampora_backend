package com.ev.ampora_backend.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Charger {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String chargerId;

    private String type;
    private double powerKw;

    @Enumerated(EnumType.STRING)
    private ChargerStatus status;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;
}


