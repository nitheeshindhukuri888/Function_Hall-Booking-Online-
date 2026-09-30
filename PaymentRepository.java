package com.example.functionhall.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
public class PaymentRepository {
    private final JdbcTemplate jdbc;
    public PaymentRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public void insert(long bookingId, String orderId, String paymentId, BigDecimal amount,
                       String status, String signature) {
        jdbc.update("""
            INSERT INTO payments(booking_id,gateway_order_id,gateway_payment_id,amount,status,signature)
            VALUES(?,?,?,?,?,?)
        """, bookingId, orderId, paymentId, amount, status, signature);
    }
}
