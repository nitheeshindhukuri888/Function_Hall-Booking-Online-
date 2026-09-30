package com.example.functionhall.repository;

import com.example.functionhall.dto.BookingRequest;
import com.example.functionhall.model.Booking;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class BookingRepository {
    private final JdbcTemplate jdbc;
    public BookingRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    private final org.springframework.jdbc.core.RowMapper<Booking> mapper = (rs, row) ->
        new Booking(
            rs.getLong("id"), rs.getLong("user_id"), rs.getLong("hall_id"),
            rs.getDate("event_date").toLocalDate(), rs.getString("event_type"),
            rs.getInt("guest_count"), rs.getString("customer_name"),
            rs.getString("customer_phone"), rs.getString("customer_email"),
            rs.getString("notes"), rs.getBigDecimal("total_amount"),
            rs.getString("status"), rs.getString("payment_status"),
            rs.getString("payment_id"), rs.getString("gateway_order_id")
        );

    public boolean isAvailable(long hallId, LocalDate date) {
        Integer count = jdbc.queryForObject("""
            SELECT COUNT(*) FROM bookings WHERE hall_id=? AND event_date=?
            AND status NOT IN ('CANCELLED','REJECTED')
        """, Integer.class, hallId, date);
        return count == null || count == 0;
    }

    public boolean isAvailableForUpdate(long hallId, LocalDate date) {
        // Lock active booking rows during the surrounding transaction.
        List<Long> ids = jdbc.query("""
            SELECT id FROM bookings
            WHERE hall_id=? AND event_date=?
            AND status NOT IN ('CANCELLED','REJECTED')
            FOR UPDATE
        """, (rs, row) -> rs.getLong("id"), hallId, date);
        return ids.isEmpty();
    }

    public long create(long userId, BookingRequest r, java.math.BigDecimal total) {
        jdbc.update("""
            INSERT INTO bookings
            (user_id,hall_id,event_date,event_type,guest_count,customer_name,
             customer_phone,customer_email,notes,total_amount)
            VALUES (?,?,?,?,?,?,?,?,?,?)
        """, userId, r.hallId(), r.eventDate(), r.eventType(), r.guestCount(),
            r.customerName(), r.customerPhone(), r.customerEmail(), r.notes(), total);
        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    public Booking findById(long id) {
        List<Booking> list = jdbc.query("SELECT * FROM bookings WHERE id=?", mapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Booking> findByUser(long userId) {
        return jdbc.query("SELECT * FROM bookings WHERE user_id=? ORDER BY created_at DESC", mapper, userId);
    }

    public List<Booking> findAll() {
        return jdbc.query("SELECT * FROM bookings ORDER BY created_at DESC", mapper);
    }

    public void setGatewayOrder(long bookingId, String orderId) {
        jdbc.update("UPDATE bookings SET gateway_order_id=? WHERE id=?", orderId, bookingId);
    }

    public void setPayment(long bookingId, String paymentId) {
        jdbc.update("""
            UPDATE bookings SET payment_status='PAID', status='CONFIRMED', payment_id=? WHERE id=?
        """, paymentId, bookingId);
    }

    public void markCancelled(long bookingId, String refundStatus) {
        jdbc.update("""
            UPDATE bookings SET status='CANCELLED', refund_status=? WHERE id=?
        """, refundStatus, bookingId);
    }
}
