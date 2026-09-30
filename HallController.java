package com.example.functionhall.controller;

import com.example.functionhall.model.Hall;
import com.example.functionhall.repository.HallRepository;
import com.example.functionhall.repository.BookingRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/halls")
public class HallController {
    private final HallRepository halls;
    private final BookingRepository bookings;

    public HallController(HallRepository halls, BookingRepository bookings) {
        this.halls = halls;
        this.bookings = bookings;
    }

    @GetMapping
    public List<Hall> all(@RequestParam(required=false) String city) {
        return halls.findAll(city);
    }

    @GetMapping("/all")
    public List<Hall> allHalls() {
        return halls.findAll(null);
    }

    @GetMapping("/{id}")
    public Hall one(@PathVariable long id) {
        return halls.findById(id);
    }

    @GetMapping("/{id}/availability")
    public Map<String, Object> availability(@PathVariable long id, @RequestParam String date) {
        LocalDate d = LocalDate.parse(date);
        return Map.of("hallId", id, "date", d, "available", bookings.isAvailable(id, d));
    }
}
