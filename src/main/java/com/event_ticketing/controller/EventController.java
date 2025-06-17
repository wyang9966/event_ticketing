package com.event_ticketing.controller;

import com.event_ticketing.dto.CreateEventRequest;
import com.event_ticketing.dto.EventResponse;
import com.event_ticketing.entity.Event;
import com.event_ticketing.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody CreateEventRequest request) {
        return ResponseEntity.ok(service.createEvent(request));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(service.getAllEventsWithInventory());
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(service.getEventWithInventory(eventId));
    }

    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<List<Event>> getRecommendedEvents(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getRecommendedEvents(userId));
    }
}
