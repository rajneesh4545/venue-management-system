package com.hcl.Venue_Management.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import com.hcl.Venue_Management.entity.Booking;
import com.hcl.Venue_Management.entity.Payment;
import com.hcl.Venue_Management.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingService bookingService;

    public PaymentService(PaymentRepository paymentRepository, BookingService bookingService) {
        this.paymentRepository = paymentRepository;
        this.bookingService = bookingService;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id " + id));
    }

    public List<Payment> getPaymentsByBooking(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    public Payment addPayment(Payment payment) {
        if (payment.getBooking() == null || payment.getBooking().getId() == null) {
            throw new RuntimeException("Booking id is required");
        }
        if (payment.getAmount() <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }
        Booking booking = bookingService.getBookingById(payment.getBooking().getId());
        payment.setBooking(booking);
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDate.now());
        }
        if (payment.getStatus() == null) {
            payment.setStatus("PAID");
        }
        return paymentRepository.save(payment);
    }

    public List<Payment> addPayments(List<Payment> payments) {
        return payments.stream().map(this::addPayment).toList();
    }

    public Payment updatePayment(Long id, Payment newData) {
        Payment payment = getPaymentById(id);
        payment.setAmount(newData.getAmount());
        payment.setPaymentDate(newData.getPaymentDate());
        payment.setMethod(newData.getMethod());
        payment.setStatus(newData.getStatus());
        return paymentRepository.save(payment);
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    public double getTotalPaid(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId).stream()
                .filter(p -> "PAID".equals(p.getStatus()))
                .mapToDouble(Payment::getAmount)
                .sum();
    }
}