package com.hcl.Venue_Management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hcl.Venue_Management.entity.Venue;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
}