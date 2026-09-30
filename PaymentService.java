package com.example.functionhall.service;

import com.example.functionhall.model.Booking;
import com.example.functionhall.repository.BookingRepository;
import com.example.functionhall.repository.PaymentRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.Order;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class PaymentService {
    private final BookingRepository bookings;
    private final PaymentRepository payments;

    @Value("${razorpay.key.id:}") private String keyId;
    @Value("${razorpay.key.secret:}") private String keySecret;

    public PaymentService(BookingRepository bookings, PaymentRepository payments) {
        this.bookings = bookings; this.payments = payments;
    }

    public Map<String,Object> createOrder(long bookingId) throws Exception {
        Booking b = bookings.findById(bookingId);
        if (b == null) throw new IllegalArgumentException("Booking not found");
        if (!"PENDING_PAYMENT".equals(b.status()))
            throw new IllegalArgumentException("Booking is not payable");
        if (keyId.isBlank() || keySecret.isBlank())
            throw new IllegalStateException("Payment gateway is not configured");

        RazorpayClient client = new RazorpayClient(keyId, keySecret);
        JSONObject req = new JSONObject();
        int paise = b.totalAmount().multiply(BigDecimal.valueOf(100)).intValueExact();
        req.put("amount", paise);
        req.put("currency", "INR");
        req.put("receipt", "booking_" + bookingId);
        Order order = client.orders.create(req);
        bookings.setGatewayOrder(bookingId, order.get("id"));
        return Map.of("keyId", keyId, "orderId", order.get("id"), "amount", paise, "currency", "INR");
    }

    @Transactional
    public void verifyAndConfirm(long bookingId, String orderId, String paymentId, String signature) throws Exception {
        Booking b = bookings.findById(bookingId);
        if (b == null) throw new IllegalArgumentException("Booking not found");
        if (!orderId.equals(b.gatewayOrderId()))
            throw new IllegalArgumentException("Order does not match booking");
        JSONObject attributes = new JSONObject();
        attributes.put("razorpay_order_id", orderId);
        attributes.put("razorpay_payment_id", paymentId);
        attributes.put("razorpay_signature", signature);
        Utils.verifyPaymentSignature(attributes, keySecret);

        if ("PAID".equals(b.paymentStatus())) return;
        payments.insert(bookingId, orderId, paymentId, b.totalAmount(), "PAID", signature);
        bookings.setPayment(bookingId, paymentId);
    }

    @Transactional
    public void cancel(long bookingId, long userId) throws Exception {
        Booking b = bookings.findById(bookingId);
        if (b == null || b.userId() != userId) throw new IllegalArgumentException("Booking not found");
        if ("CANCELLED".equals(b.status())) return;

        if ("PAID".equals(b.paymentStatus())) {
            if (keyId.isBlank() || keySecret.isBlank()) throw new IllegalStateException("Payment gateway is not configured");
            RazorpayClient client = new RazorpayClient(keyId, keySecret);
            client.payments.refund(b.paymentId(), new JSONObject());
            bookings.markCancelled(bookingId, "REFUNDED");
        } else {
            bookings.markCancelled(bookingId, "NOT_REQUIRED");
        }
    }
}
