package com.example.functionhall.service;

import com.example.functionhall.dto.BookingRequest;
import com.example.functionhall.model.Booking;
import com.example.functionhall.model.Hall;
import com.example.functionhall.repository.BookingRepository;
import com.example.functionhall.repository.HallRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookings;
    private final HallRepository halls;

    public BookingService(BookingRepository bookings, HallRepository halls) {
        this.bookings = bookings; this.halls = halls;
    }

    @Transactional
    public Booking create(long userId, BookingRequest r) {
        Hall hall = halls.findById(r.hallId());
        if (hall == null || !hall.active()) throw new IllegalArgumentException("Hall is unavailable");
        if (r.guestCount() > hall.capacity())
            throw new IllegalArgumentException("Guest count exceeds hall capacity");
        // Repository locks the hall/date check and insert in one transaction.
        if (!bookings.isAvailableForUpdate(r.hallId(), r.eventDate()))
            throw new IllegalArgumentException("Hall is already booked for this date");

        long id = bookings.create(userId, r, hall.pricePerDay());
        return bookings.findById(id);
    }

    public List<Booking> byUser(long userId) { return bookings.findByUser(userId); }
}
