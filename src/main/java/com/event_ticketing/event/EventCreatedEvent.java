package com.event_ticketing.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCreatedEvent {
    private Long eventId;
    private String eventName;
    private String eventDate;
    private Integer totalTickets;
    private LocalDateTime createdAt;
} 