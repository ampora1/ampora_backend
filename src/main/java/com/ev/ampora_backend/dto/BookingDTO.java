package com.ev.ampora_backend.dto;

import com.ev.ampora_backend.entity.BookingStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {

    private String bookingId;

    private String userId;
    private String chargerId;

    private LocalDate date;

    private String startTime;
    private String endTime;
    private double amount;
    private String ChargerType;

    private BookingStatus status;
}
