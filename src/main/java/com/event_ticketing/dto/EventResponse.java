package com.event_ticketing.dto;

import lombok.Data;

@Data
public class EventResponse {
    private Long id;
    private String name;
    private String eventDate;
    private Integer totalTickets;
    private Integer availableTickets;
    private Integer soldTickets;
    
    public EventResponse(Long id, String name, String eventDate, Integer totalTickets, 
                        Integer availableTickets, Integer soldTickets) {
        this.id = id;
        this.name = name;
        this.eventDate = eventDate;
        this.totalTickets = totalTickets;
        this.availableTickets = availableTickets;
        this.soldTickets = soldTickets;
    }
} 