package com.hcl.Venue_Management.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hcl.Venue_Management.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByVenueId(Long venueId);

    List<Booking> findByVenueIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long venueId, LocalDate endDate, LocalDate startDate);
}