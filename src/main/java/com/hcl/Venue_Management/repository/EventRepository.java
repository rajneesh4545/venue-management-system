package com.hcl.Venue_Management.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hcl.Venue_Management.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByVenueId(Long venueId);
}
