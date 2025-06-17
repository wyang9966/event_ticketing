package com.event_ticketing.service;

import com.event_ticketing.dto.CreateEventRequest;
import com.event_ticketing.dto.EventResponse;
import com.event_ticketing.entity.Event;
import com.event_ticketing.entity.EventInventory;
import com.event_ticketing.event.EventCreatedEvent;
import com.event_ticketing.repository.EventInventoryRepository;
import com.event_ticketing.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository repository;
    private final EventInventoryRepository inventoryRepository;
    private final TicketService ticketService;
    private final KafkaEventPublisher kafkaEventPublisher;

    public EventService(EventRepository repository, EventInventoryRepository inventoryRepository,
                       TicketService ticketService, KafkaEventPublisher kafkaEventPublisher) {
        this.repository = repository;
        this.inventoryRepository = inventoryRepository;
        this.ticketService = ticketService;
        this.kafkaEventPublisher = kafkaEventPublisher;
    }

    @Transactional
    public Event createEvent(CreateEventRequest request) {
        Event event = new Event();
        event.setName(request.getName());
        event.setEventDate(request.getEventDate());
        event.setTotalTickets(request.getTotalTickets());
        event.validate();
        
        Event savedEvent = repository.save(event);
        
        // Create inventory
        EventInventory inventory = new EventInventory();
        inventory.setEventId(savedEvent.getId());
        inventory.setAvailableTickets(request.getTotalTickets());
        inventoryRepository.save(inventory);
        
        // Publish event
        EventCreatedEvent eventCreatedEvent = new EventCreatedEvent(
                savedEvent.getId(), savedEvent.getName(), savedEvent.getEventDate(),
                savedEvent.getTotalTickets(), LocalDateTime.now());
        kafkaEventPublisher.publishEventCreated(eventCreatedEvent);
        
        return savedEvent;
    }

    public List<Event> getAllEvents() {
        return repository.findAll();
    }

    public List<EventResponse> getAllEventsWithInventory() {
        return repository.findAll().stream()
                .map(this::convertToEventResponse)
                .collect(Collectors.toList());
    }

    public EventResponse getEventWithInventory(Long eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return convertToEventResponse(event);
    }

    private EventResponse convertToEventResponse(Event event) {
        long soldTickets = ticketService.getSoldTicketsCount(event.getId());
        long availableTickets = ticketService.getAvailableTicketsCount(event.getId());
        
        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getEventDate(),
                event.getTotalTickets(),
                (int) availableTickets,
                (int) soldTickets
        );
    }

    /**
     * TODO: Implement Spring AI recommendation system
     * - Add Spring AI dependencies to pom.xml
     * - Configure OpenAI API keys in application.properties
     * - Implement user preference analysis
     * - Add event similarity scoring
     * - Create personalized recommendation algorithm
     * - Add recommendation caching for performance
     */
    public List<Event> getRecommendedEvents(Long userId) {
        // TODO: Replace with Spring AI-powered recommendations
        // For now, return all events as a simple fallback
        return getAllEvents();
    }
}