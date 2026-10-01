package com.hcl.Venue_Management.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.hcl.Venue_Management.entity.Booking;
import com.hcl.Venue_Management.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<Booking> getAll() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public Booking getOne(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping("/venue/{venueId}")
    public List<Booking> getByVenue(@PathVariable Long venueId) {
        return bookingService.getBookingsByVenue(venueId);
    }

    @PostMapping
    public Booking add(@RequestBody Booking booking) {
        return bookingService.addBooking(booking);
    }

    @PostMapping("/bulk")
    public List<Booking> addMany(@RequestBody List<Booking> bookings) {
        return bookingService.addBookings(bookings);
    }

    @PutMapping("/{id}")
    public Booking update(@PathVariable Long id, @RequestBody Booking booking) {
        return bookingService.updateBooking(id, booking);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return "Booking deleted";
    }
}