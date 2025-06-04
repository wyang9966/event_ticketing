package com.event_ticketing.event_ticketing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "purchase_time", nullable = false)
    private LocalDateTime purchaseTime;

    // Domain logic
    public void validate() {
        if (eventId == null || userId == null) {
            throw new IllegalStateException("Invalid order");
        }
    }
}
