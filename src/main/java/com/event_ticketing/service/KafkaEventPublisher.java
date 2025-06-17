package com.event_ticketing.service;

import com.event_ticketing.event.EventCreatedEvent;
import com.event_ticketing.event.TicketPurchasedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka Event Publisher for real-time event streaming
 * - Publishes events to Kafka topics for scalable processing
 * - Handles async publishing with error handling
 * - Supports event sourcing and microservices communication
 * - Gracefully handles when Kafka is disabled
 */
@Service
public class KafkaEventPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaEventPublisher.class);
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    @Value("${spring.kafka.enabled:true}")
    private boolean kafkaEnabled;
    
    // Kafka Topics
    private static final String TICKET_PURCHASED_TOPIC = "ticket-purchased";
    private static final String EVENT_CREATED_TOPIC = "event-created";
    private static final String ORDER_CREATED_TOPIC = "order-created";

    public KafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Publishes ticket purchased event to Kafka
     * This enables scalable, asynchronous processing of purchases
     */
    public void publishTicketPurchased(TicketPurchasedEvent event) {
        if (!kafkaEnabled) {
            logger.info("Kafka disabled - skipping ticket purchased event: {}", event.getTicketId());
            return;
        }
        
        try {
            String key = event.getEventId() + "-" + event.getTicketId();
            CompletableFuture<SendResult<String, Object>> future = 
                kafkaTemplate.send(TICKET_PURCHASED_TOPIC, key, event);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Ticket purchased event published successfully: {}", event.getTicketId());
                } else {
                    logger.error("Failed to publish ticket purchased event: {}", ex.getMessage());
                }
            });
        } catch (Exception e) {
            logger.error("Error publishing ticket purchased event: {}", e.getMessage(), e);
        }
    }

    /**
     * Publishes event created event to Kafka
     * Enables other services to react to new events
     */
    public void publishEventCreated(EventCreatedEvent event) {
        if (!kafkaEnabled) {
            logger.info("Kafka disabled - skipping event created: {}", event.getEventId());
            return;
        }
        
        try {
            String key = event.getEventId().toString();
            CompletableFuture<SendResult<String, Object>> future = 
                kafkaTemplate.send(EVENT_CREATED_TOPIC, key, event);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Event created published successfully: {}", event.getEventId());
                } else {
                    logger.error("Failed to publish event created: {}", ex.getMessage());
                }
            });
        } catch (Exception e) {
            logger.error("Error publishing event created: {}", e.getMessage(), e);
        }
    }
}
