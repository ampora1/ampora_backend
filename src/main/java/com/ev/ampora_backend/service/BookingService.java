package com.ev.ampora_backend.service;

import com.ev.ampora_backend.dto.AvailabilityResponse;
import com.ev.ampora_backend.dto.BookingDTO;
import com.ev.ampora_backend.entity.Booking;
import com.ev.ampora_backend.entity.BookingStatus;
import com.ev.ampora_backend.entity.Charger;
import com.ev.ampora_backend.entity.User;
import com.ev.ampora_backend.repository.BookingRepository;
import com.ev.ampora_backend.repository.ChargerRepository;
import com.ev.ampora_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepo;
    private final UserRepository userRepo;
    private final ChargerRepository chargerRepo;

    /* -----------------------------------------------------------
       FIX: Safely parse time (handles invalid 24:xx format)
    ------------------------------------------------------------ */
    private LocalDateTime safeParseTime(String time) {

        return LocalDateTime.parse(time);
    }

    /* -----------------------------------------------------------
       AVAILABILITY CHECK
    ------------------------------------------------------------ */
    public AvailabilityResponse isAvailable(String chargerId, LocalDate date, String start, String end) {

        LocalTime startTime = LocalTime.parse(start);
        LocalTime endTime = LocalTime.parse(end);

        LocalDateTime startDT = LocalDateTime.of(date, LocalTime.from(startTime));
        LocalDateTime endDT = LocalDateTime.of(date, LocalTime.from(endTime));

        List<Booking> overlapping = bookingRepo
                .findOverlappingBookings(chargerId, startDT, endDT);

        boolean available = overlapping.isEmpty();

        return AvailabilityResponse.builder()
                .available(available)
                .message(available ? "Slot is available" : "This time slot is already booked.")
                .build();
    }

    /* -----------------------------------------------------------
       CREATE BOOKING
    ------------------------------------------------------------ */
    public BookingDTO createBooking(BookingDTO dto) {

        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Charger charger = chargerRepo.findById(dto.getChargerId())
                .orElseThrow(() -> new RuntimeException("Charger not found"));

        if (dto.getDate() == null)
            throw new RuntimeException("date is required");

        LocalTime start = LocalTime.parse(dto.getStartTime());
        LocalTime end = LocalTime.parse(dto.getEndTime());

        LocalDateTime startDT = LocalDateTime.of(dto.getDate(), start);
        LocalDateTime endDT = LocalDateTime.of(dto.getDate(), end);

        boolean available = bookingRepo
                .findOverlappingBookings(dto.getChargerId(), startDT, endDT)
                .isEmpty();

        if (!available)
            throw new RuntimeException("This time slot is already booked.");

        Booking booking = Booking.builder()
                .user(user)
                .charger(charger)
                .startTime(startDT)
                .endTime(endDT)
                .bookingStatus(BookingStatus.PENDING)
                .build();

        Booking saved = bookingRepo.save(booking);

        return BookingDTO.builder()
                .bookingId(saved.getBookingId())
                .userId(user.getUserId())
                .chargerId(charger.getChargerId())
                .date(dto.getDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .status(saved.getBookingStatus())
                .build();
    }


    /* -----------------------------------------------------------
       GET USER BOOKINGS
    ------------------------------------------------------------ */
    public List<BookingDTO> getBookingsForUser(String userId) {

        return bookingRepo.findByUser_UserId(userId).stream().map(b ->
                BookingDTO.builder()
                        .bookingId(b.getBookingId())
                        .userId(b.getUser().getUserId())
                        .chargerId(b.getCharger().getChargerId())
                        .date(b.getStartTime().toLocalDate())
                        .startTime(b.getStartTime().toLocalTime().toString())
                        .endTime(b.getEndTime().toLocalTime().toString())
                        .status(b.getBookingStatus())
                        .amount(1000)
                        .ChargerType(b.getCharger().getType())
                        .build()
        ).toList();
    }

    /* -----------------------------------------------------------
       CANCEL BOOKING
    ------------------------------------------------------------ */
    public void cancelBooking(String bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepo.save(booking);
    }

    public List<BookingDTO> getAllBookings(){
        return  bookingRepo.findAll().stream().map(b->BookingDTO.builder().bookingId(b.getBookingId())
                .userId(b.getUser().getUserId())
                .chargerId(b.getCharger().getChargerId())
                .date(b.getStartTime().toLocalDate())
                .startTime(b.getStartTime().toLocalTime().toString())
                .endTime(b.getEndTime().toLocalTime().toString())
                .status(b.getBookingStatus())
                .amount(1000)
                .ChargerType(b.getCharger().getType()).build()).toList();
    }
    public BookingDTO updateBooking(BookingDTO dto) {

        if (dto.getBookingId() == null)
            throw new RuntimeException("bookingId is required");

        Booking booking = bookingRepo.findById(dto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Charger charger = chargerRepo.findById(dto.getChargerId())
                .orElseThrow(() -> new RuntimeException("Charger not found"));

        LocalTime start = LocalTime.parse(dto.getStartTime());
        LocalTime end = LocalTime.parse(dto.getEndTime());

        LocalDateTime startDT = LocalDateTime.of(dto.getDate(), start);
        LocalDateTime endDT = LocalDateTime.of(dto.getDate(), end);

        boolean available = bookingRepo
                .findOverlapsForUpdate(dto.getChargerId(), dto.getBookingId(), startDT, endDT)
                .isEmpty();

        if (!available)
            throw new RuntimeException("This time slot is already booked.");

        booking.setUser(user);
        booking.setCharger(charger);
        booking.setStartTime(startDT);
        booking.setEndTime(endDT);
        booking.setBookingStatus(dto.getStatus());

        Booking saved = bookingRepo.save(booking);

        return BookingDTO.builder()
                .bookingId(saved.getBookingId())
                .userId(user.getUserId())
                .chargerId(charger.getChargerId())
                .date(saved.getStartTime().toLocalDate())
                .startTime(saved.getStartTime().toLocalTime().toString())
                .endTime(saved.getEndTime().toLocalTime().toString())
                .status(saved.getBookingStatus())
                .build();
    }

}
