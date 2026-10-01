package com.hcl.Venue_Management.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.hcl.Venue_Management.entity.Amenity;
import com.hcl.Venue_Management.entity.Venue;
import com.hcl.Venue_Management.repository.AmenityRepository;
import com.hcl.Venue_Management.repository.VenueRepository;

@Service
public class AmenityService {

    private final AmenityRepository amenityRepository;
    private final VenueRepository venueRepository;
    private final VenueService venueService;

    public AmenityService(AmenityRepository amenityRepository,
                          VenueRepository venueRepository,
                          VenueService venueService) {
        this.amenityRepository = amenityRepository;
        this.venueRepository = venueRepository;
        this.venueService = venueService;
    }

    public List<Amenity> getAllAmenities() {
        return amenityRepository.findAll();
    }

    public Amenity getAmenityById(Long id) {
        return amenityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Amenity not found with id " + id));
    }

    public Amenity addAmenity(Amenity amenity) {
        return amenityRepository.save(amenity);
    }

    public List<Amenity> addAmenities(List<Amenity> amenities) {
        return amenityRepository.saveAll(amenities);
    }

    public Amenity updateAmenity(Long id, Amenity newData) {
        Amenity amenity = getAmenityById(id);
        amenity.setName(newData.getName());
        amenity.setPrice(newData.getPrice());
        amenity.setDescription(newData.getDescription());
        return amenityRepository.save(amenity);
    }

    public void deleteAmenity(Long id) {
        amenityRepository.deleteById(id);
    }

    public Venue addAmenityToVenue(Long venueId, Long amenityId) {
        Venue venue = venueService.getVenueById(venueId);
        Amenity amenity = getAmenityById(amenityId);
        venue.getAmenities().add(amenity);
        return venueRepository.save(venue);
    }

    public Venue removeAmenityFromVenue(Long venueId, Long amenityId) {
        Venue venue = venueService.getVenueById(venueId);
        venue.getAmenities().removeIf(a -> a.getId().equals(amenityId));
        return venueRepository.save(venue);
    }
}