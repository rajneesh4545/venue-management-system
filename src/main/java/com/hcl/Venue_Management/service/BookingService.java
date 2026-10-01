package com.hcl.Venue_Management.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.hcl.Venue_Management.entity.Booking;
import com.hcl.Venue_Management.entity.Venue;
import com.hcl.Venue_Management.repository.BookingRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final VenueService venueService;

    public BookingService(BookingRepository bookingRepository, VenueService venueService) {
        this.bookingRepository = bookingRepository;
        this.venueService = venueService;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id " + id));
    }

    public List<Booking> getBookingsByVenue(Long venueId) {
        return bookingRepository.findByVenueId(venueId);
    }

    public Booking addBooking(Booking booking) {
        validateAndAttachVenue(booking, null);
        booking.setStatus("CONFIRMED");
        return bookingRepository.save(booking);
    }

    public List<Booking> addBookings(List<Booking> bookings) {
        bookings.forEach(b -> validateAndAttachVenue(b, null));
        return bookingRepository.saveAll(bookings);
    }

    public Booking updateBooking(Long id, Booking newData) {
        Booking booking = getBookingById(id);
        validateAndAttachVenue(newData, id);
        booking.setCustomerName(newData.getCustomerName());
        booking.setCustomerEmail(newData.getCustomerEmail());
        booking.setStartDate(newData.getStartDate());
        booking.setEndDate(newData.getEndDate());
        booking.setStatus(newData.getStatus());
        booking.setVenue(newData.getVenue());
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    private void validateAndAttachVenue(Booking booking, Long bookingIdToIgnore) {
        if (booking.getVenue() == null || booking.getVenue().getId() == null) {
            throw new RuntimeException("Venue id is required");
        }
        if (booking.getStartDate() == null || booking.getEndDate() == null
                || booking.getEndDate().isBefore(booking.getStartDate())) {
            throw new RuntimeException("Invalid dates: end date must be on or after start date");
        }

        Venue venue = venueService.getVenueById(booking.getVenue().getId());
        booking.setVenue(venue);

        boolean clash = bookingRepository
                .findByVenueIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        venue.getId(), booking.getEndDate(), booking.getStartDate())
                .stream()
                .anyMatch(b -> !b.getId().equals(bookingIdToIgnore));

        if (clash) {
            throw new RuntimeException("Venue is already booked for these dates");
        }
    }
}