package com.event_ticketing.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketPurchasedEvent {
    private Long ticketId;
    private Long eventId;
    private Long userId;
    private String status;
    private LocalDateTime purchaseTime;
    private Double amount;
    private String paymentId;
} 