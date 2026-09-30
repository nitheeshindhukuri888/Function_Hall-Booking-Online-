package com.example.functionhall.controller;

import com.example.functionhall.model.Booking;
import com.example.functionhall.repository.BookingRepository;
import com.example.functionhall.repository.HallRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final BookingRepository bookings;
    private final HallRepository halls;

    @Value("${app.admin.email}")
    private String adminEmail;
    @Value("${app.admin.password}")
    private String adminPassword;

    public AdminController(BookingRepository bookings, HallRepository halls) {
        this.bookings = bookings; this.halls = halls;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String,String> body) {
        boolean ok = adminEmail.equals(body.get("email")) && adminPassword.equals(body.get("password"));
        return Map.of("success", ok, "message", ok ? "Admin login successful" : "Invalid admin credentials");
    }

    @GetMapping("/bookings")
    public List<Booking> bookings() {
        return bookings.findAll();
    }

    @DeleteMapping("/halls/{id}")
    public Map<String, String> deactivateHall(@PathVariable long id) {
        halls.delete(id);
        return Map.of("message", "Hall deactivated");
    }
}
