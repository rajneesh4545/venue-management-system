package com.hcl.Venue_Management.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.hcl.Venue_Management.entity.Event;
import com.hcl.Venue_Management.entity.Venue;
import com.hcl.Venue_Management.repository.EventRepository;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final VenueService venueService;

    public EventService(EventRepository eventRepository, VenueService venueService) {
        this.eventRepository = eventRepository;
        this.venueService = venueService;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id " + id));
    }

    public List<Event> getEventsByVenue(Long venueId) {
        return eventRepository.findByVenueId(venueId);
    }

    public Event addEvent(Event event) {
        attachVenue(event);
        return eventRepository.save(event);
    }

    public List<Event> addEvents(List<Event> events) {
        events.forEach(this::attachVenue);
        return eventRepository.saveAll(events);
    }

    public Event updateEvent(Long id, Event newData) {
        Event event = getEventById(id);
        attachVenue(newData);
        event.setTitle(newData.getTitle());
        event.setEventType(newData.getEventType());
        event.setEventDate(newData.getEventDate());
        event.setExpectedGuests(newData.getExpectedGuests());
        event.setVenue(newData.getVenue());
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    private void attachVenue(Event event) {
        if (event.getVenue() == null || event.getVenue().getId() == null) {
            throw new RuntimeException("Venue id is required");
        }
        Venue venue = venueService.getVenueById(event.getVenue().getId());
        if (event.getExpectedGuests() > venue.getCapacity()) {
            throw new RuntimeException("Guests exceed venue capacity of " + venue.getCapacity());
        }
        event.setVenue(venue);
    }
}