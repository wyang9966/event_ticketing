package com.event_ticketing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateEventRequest {
    @NotBlank(message = "Event name is required")
    private String name;
    
    private String description;
    
    @NotBlank(message = "Event date is required")
    private String date;
    
    private String venue;
    
    @NotNull(message = "Total tickets is required")
    @Positive(message = "Total tickets must be positive")
    private Integer totalTickets;
    
    private Double price;
    
    // Getter for backward compatibility
    public String getEventDate() {
        return date;
    }
} 