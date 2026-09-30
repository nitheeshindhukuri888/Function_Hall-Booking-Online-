package com.example.functionhall.controller;

import com.example.functionhall.dto.BookingRequest;
import com.example.functionhall.model.Booking;
import com.example.functionhall.service.BookingService;
import com.example.functionhall.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookings;
    private final PaymentService payments;

    public BookingController(BookingService bookings, PaymentService payments) {
        this.bookings = bookings; this.payments = payments;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestParam long userId, @Valid @RequestBody BookingRequest r) {
        try {
            Booking b = bookings.create(userId, r);
            return ResponseEntity.ok(Map.of("bookingId", b.id(), "amount", b.totalAmount(),
                    "message","Booking created. Complete payment to confirm."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message",e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public List<Booking> byUser(@PathVariable long userId) { return bookings.byUser(userId); }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<?> cancel(@PathVariable long bookingId, @RequestParam long userId) {
        try {
            payments.cancel(bookingId,userId);
            return ResponseEntity.ok(Map.of("message","Booking cancelled"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message",e.getMessage()));
        }
    }
}
