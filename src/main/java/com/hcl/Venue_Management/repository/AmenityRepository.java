package com.hcl.Venue_Management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hcl.Venue_Management.entity.Amenity;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {
}