package com.event_ticketing.event_ticketing.entity;

import com.event_ticketing.event_ticketing.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tickets")
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String status; // e.g., PURCHASED, RESERVED

    // Domain logic
    public void purchase(Long eventId, Long userId) {
        this.eventId = eventId;
        this.userId = userId;
        this.status = String.valueOf(TicketStatus.PURCHASED);
    }
}
