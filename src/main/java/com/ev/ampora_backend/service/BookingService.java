package com.ev.ampora_backend.service;

import com.ev.ampora_backend.dto.AvailabilityResponse;
import com.ev.ampora_backend.dto.BookingDTO;
import com.ev.ampora_backend.entity.*;
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


    private LocalDateTime safeParseTime(String time) {

        return LocalDateTime.parse(time);
    }


    public AvailabilityResponse isAvailable(String chargerId, LocalDate date, LocalTime start, LocalTime end) {

        LocalTime startTime = start;
        LocalTime endTime = end;

//        LocalDateTime startDT = LocalDateTime.of(date, startTime);
//        LocalDateTime endDT = LocalDateTime.of(date, endTime);

        List<Booking> overlapping = bookingRepo
                .findOverlappingBookings(chargerId, startTime, endTime);

        boolean available = overlapping.isEmpty();

        return AvailabilityResponse.builder()
                .available(available)
                .message(available ? "Slot is available" : "This time slot is already booked.")
                .build();
    }


    public BookingDTO createBooking(BookingDTO dto) {
        System.out.println("Booking created");
        System.out.println("dto: " + dto.toString());
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Charger charger = chargerRepo.findById(dto.getChargerId())
                .orElseThrow(() -> new RuntimeException("Charger not found"));

        if (dto.getDate() == null)
            throw new RuntimeException("date is required");

        LocalTime start = dto.getStartTime();
        LocalTime end = dto.getEndTime();

        LocalDateTime startDT = LocalDateTime.of(dto.getDate(), start);
        LocalDateTime endDT = LocalDateTime.of(dto.getDate(), end);

        boolean available = bookingRepo
                .findOverlappingBookings(dto.getChargerId(), start, end)
                .isEmpty();

        if (!available)
            throw new RuntimeException("This time slot is already booked.");

        Booking booking = Booking.builder()
                .user(user)
                .charger(charger)
                .startTime(LocalTime.from(startDT))
                .endTime(LocalTime.from(endDT))
                .bookingFee(300)
                .paymentStatus(PaymentStatus.PENDING)
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

    public String createPendingBooking(BookingDTO dto) {
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Charger charger = chargerRepo.findById(dto.getChargerId())
                .orElseThrow(() -> new RuntimeException("Charger not found"));
        Booking booking = Booking.builder()
                .user(user)
                .charger(charger)
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .date(dto.getDate())
                .bookingFee((int) dto.getAmount())
                .bookingStatus(BookingStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        bookingRepo.save(booking);
        System.out.println("Booking created"+booking.getBookingId());

        return booking.getBookingId(); // UUID / generated ID
    }


    public void confirmPayment(String bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setPaymentStatus(PaymentStatus.SUCCESS);
        booking.setBookingStatus(BookingStatus.CONFIRMED);

        bookingRepo.save(booking);
    }
    public List<BookingDTO> getBookingsForUser(String userId) {

        return bookingRepo.findByUser_UserId(userId).stream().map(b ->
                BookingDTO.builder()
                        .bookingId(b.getBookingId())
                        .userId(b.getUser().getUserId())
                        .chargerId(b.getCharger().getChargerId())
                        .date(b.getDate())
                        .startTime(b.getStartTime())
                        .endTime(b.getEndTime())
                        .status(b.getBookingStatus())
                        .amount(1000)
                        .ChargerType(b.getCharger().getType())
                        .build()
        ).toList();
    }


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
              .date(b.getDate())
                .startTime(b.getStartTime())
                .endTime(b.getEndTime())
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

        LocalTime start = dto.getStartTime();
        LocalTime end = dto.getEndTime();

        LocalDateTime startDT = LocalDateTime.of(dto.getDate(), start);
        LocalDateTime endDT = LocalDateTime.of(dto.getDate(), end);

        boolean available = bookingRepo
                .findOverlapsForUpdate(dto.getChargerId(), dto.getBookingId(), startDT, endDT)
                .isEmpty();

        if (!available)
            throw new RuntimeException("This time slot is already booked.");

        booking.setUser(user);
        booking.setCharger(charger);
        booking.setStartTime(LocalTime.from(startDT));
        booking.setEndTime(LocalTime.from(endDT));
        booking.setBookingStatus(dto.getStatus());

        Booking saved = bookingRepo.save(booking);

        return BookingDTO.builder()
                .bookingId(saved.getBookingId())
                .userId(user.getUserId())
                .chargerId(charger.getChargerId())
                .date(LocalDate.from(saved.getStartTime()))
                .startTime(LocalTime.from(saved.getStartTime()))
                .endTime(LocalTime.from(saved.getEndTime()))
                .status(saved.getBookingStatus())
                .build();
    }

}
