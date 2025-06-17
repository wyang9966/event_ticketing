package com.event_ticketing.controller;

import com.event_ticketing.dto.PurchaseTicketRequest;
import com.event_ticketing.entity.Ticket;
import com.event_ticketing.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<Ticket>> purchaseTickets(@Valid @RequestBody PurchaseTicketRequest request) {
        return ResponseEntity.ok(ticketService.purchaseTickets(request));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Ticket>> getTicketsByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(ticketService.getTicketsByEvent(eventId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getTicketsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getTicketsByUser(userId));
    }

    @GetMapping("/event/{eventId}/count")
    public ResponseEntity<Long> getSoldTicketsCount(@PathVariable Long eventId) {
        return ResponseEntity.ok(ticketService.getSoldTicketsCount(eventId));
    }

    @GetMapping("/event/{eventId}/available")
    public ResponseEntity<Long> getAvailableTicketsCount(@PathVariable Long eventId) {
        return ResponseEntity.ok(ticketService.getAvailableTicketsCount(eventId));
    }
} 