package com.hcl.Venue_Management.entity;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private LocalDate paymentDate;
    private String method;   // CASH, UPI, CARD, BANK_TRANSFER
    private String status;   // PAID, PENDING, REFUNDED

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
}
