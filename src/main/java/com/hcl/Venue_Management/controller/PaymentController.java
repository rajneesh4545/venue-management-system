package com.hcl.Venue_Management.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.hcl.Venue_Management.entity.Payment;
import com.hcl.Venue_Management.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<Payment> getAll() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public Payment getOne(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @GetMapping("/booking/{bookingId}")
    public List<Payment> getByBooking(@PathVariable Long bookingId) {
        return paymentService.getPaymentsByBooking(bookingId);
    }

    @GetMapping("/booking/{bookingId}/total")
    public double getTotal(@PathVariable Long bookingId) {
        return paymentService.getTotalPaid(bookingId);
    }

    @PostMapping
    public Payment add(@RequestBody Payment payment) {
        return paymentService.addPayment(payment);
    }

    @PostMapping("/bulk")
    public List<Payment> addMany(@RequestBody List<Payment> payments) {
        return paymentService.addPayments(payments);
    }

    @PutMapping("/{id}")
    public Payment update(@PathVariable Long id, @RequestBody Payment payment) {
        return paymentService.updatePayment(id, payment);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return "Payment deleted";
    }
}