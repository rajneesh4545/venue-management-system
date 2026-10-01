package com.hcl.Venue_Management.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.hcl.Venue_Management.entity.Event;
import com.hcl.Venue_Management.service.EventService;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<Event> getAll() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public Event getOne(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @GetMapping("/venue/{venueId}")
    public List<Event> getByVenue(@PathVariable Long venueId) {
        return eventService.getEventsByVenue(venueId);
    }

    @PostMapping
    public Event add(@RequestBody Event event) {
        return eventService.addEvent(event);
    }

    @PostMapping("/bulk")
    public List<Event> addMany(@RequestBody List<Event> events) {
        return eventService.addEvents(events);
    }

    @PutMapping("/{id}")
    public Event update(@PathVariable Long id, @RequestBody Event event) {
        return eventService.updateEvent(id, event);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "Event deleted";
    }
}