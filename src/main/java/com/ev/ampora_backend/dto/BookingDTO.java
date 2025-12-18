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

    private String startTime;  // "15:30"
    private String endTime;    // "16:30"

    private BookingStatus status;
}
