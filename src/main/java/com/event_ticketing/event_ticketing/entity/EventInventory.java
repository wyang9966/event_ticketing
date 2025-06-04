package com.event_ticketing.event_ticketing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "event_inventory")
@Getter
@Setter
public class EventInventory {
    @Id
    @Column(name = "event_id")
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