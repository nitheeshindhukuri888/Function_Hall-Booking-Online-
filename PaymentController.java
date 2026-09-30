package com.example.functionhall.controller;

import com.example.functionhall.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService payments;
    public PaymentController(PaymentService payments) { this.payments = payments; }

    @PostMapping("/order/{bookingId}")
    public ResponseEntity<?> createOrder(@PathVariable long bookingId) {
        try { return ResponseEntity.ok(payments.createOrder(bookingId)); }
        catch (Exception e) { return ResponseEntity.badRequest().body(Map.of("message",e.getMessage())); }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String,String> body) {
        try {
            payments.verifyAndConfirm(
                Long.parseLong(body.get("bookingId")),
                body.get("razorpay_order_id"),
                body.get("razorpay_payment_id"),
                body.get("razorpay_signature")
            );
            return ResponseEntity.ok(Map.of("message","Payment verified and booking confirmed"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message","Payment verification failed: "+e.getMessage()));
        }
    }
}
