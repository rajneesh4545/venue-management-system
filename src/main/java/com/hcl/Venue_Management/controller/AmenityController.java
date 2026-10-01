package com.hcl.Venue_Management.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.hcl.Venue_Management.entity.Amenity;
import com.hcl.Venue_Management.entity.Venue;
import com.hcl.Venue_Management.service.AmenityService;

@RestController
@RequestMapping("/api/amenities")
public class AmenityController {

    private final AmenityService amenityService;

    public AmenityController(AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @GetMapping
    public List<Amenity> getAll() {
        return amenityService.getAllAmenities();
    }

    @GetMapping("/{id}")
    public Amenity getOne(@PathVariable Long id) {
        return amenityService.getAmenityById(id);
    }

    @PostMapping
    public Amenity add(@RequestBody Amenity amenity) {
        return amenityService.addAmenity(amenity);
    }

    @PostMapping("/bulk")
    public List<Amenity> addMany(@RequestBody List<Amenity> amenities) {
        return amenityService.addAmenities(amenities);
    }

    @PutMapping("/{id}")
    public Amenity update(@PathVariable Long id, @RequestBody Amenity amenity) {
        return amenityService.updateAmenity(id, amenity);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        amenityService.deleteAmenity(id);
        return "Amenity deleted";
    }

    @PostMapping("/venue/{venueId}/add/{amenityId}")
    public Venue addToVenue(@PathVariable Long venueId, @PathVariable Long amenityId) {
        return amenityService.addAmenityToVenue(venueId, amenityId);
    }

    @DeleteMapping("/venue/{venueId}/remove/{amenityId}")
    public Venue removeFromVenue(@PathVariable Long venueId, @PathVariable Long amenityId) {
        return amenityService.removeAmenityFromVenue(venueId, amenityId);
    }
}