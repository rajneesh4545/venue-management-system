package com.hcl.Venue_Management.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.hcl.Venue_Management.entity.Venue;
import com.hcl.Venue_Management.repository.VenueRepository;

@Service
public class VenueService {

    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Venue getVenueById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue not found with id " + id));
    }

    public Venue addVenue(Venue venue) {
        return venueRepository.save(venue);
    }


    public List<Venue> addVenues(List<Venue> venues) {
        return venueRepository.saveAll(venues);
    }

    public Venue updateVenue(Long id, Venue newData) {
        Venue venue = getVenueById(id);
        venue.setName(newData.getName());
        venue.setLocation(newData.getLocation());
        venue.setCapacity(newData.getCapacity());
        venue.setPricePerDay(newData.getPricePerDay());
        venue.setDescription(newData.getDescription());
        return venueRepository.save(venue);
    }

    public void deleteVenue(Long id) {
        venueRepository.deleteById(id);
    }
}