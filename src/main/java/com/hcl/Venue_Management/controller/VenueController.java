package com.hcl.Venue_Management.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.hcl.Venue_Management.entity.Venue;
import com.hcl.Venue_Management.service.VenueService;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }


    @GetMapping
    public List<Venue> getAll() {
        return venueService.getAllVenues();
    }


    @GetMapping("/{id}")
    public Venue getOne(@PathVariable Long id) {
        return venueService.getVenueById(id);
    }


    @PostMapping
    public Venue add(@RequestBody Venue venue) {
        return venueService.addVenue(venue);
    }


    @PostMapping("/bulk")
    public List<Venue> addMany(@RequestBody List<Venue> venues) {
        return venueService.addVenues(venues);
    }


    @PutMapping("/{id}")
    public Venue update(@PathVariable Long id, @RequestBody Venue venue) {
        return venueService.updateVenue(id, venue);
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        venueService.deleteVenue(id);
        return "Venue deleted";
    }
}