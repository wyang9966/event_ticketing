package com.event_ticketing.event_ticketing.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "events")
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "event_date", nullable = false)
    private String eventDate;

    @Column(name = "total_tickets", nullable = false)
    private int totalTickets;

    public void validate() {
        if (totalTickets < 0) {
            throw new IllegalStateException("Total tickets cannot be negative");
        }
    }
}
