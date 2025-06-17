package com.event_ticketing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "event_inventory")
@Getter
@Setter
public class EventInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    private Long eventId;

    @Column(name = "available_tickets", nullable = false)
    private int availableTickets;

    // Domain logic
    public void reduceInventory() {
        if (availableTickets <= 0) {
            throw new IllegalStateException("No tickets available");
        }
        availableTickets--;
    }
}