package com.event_ticketing.service;

import com.event_ticketing.event.TicketPurchasedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka Consumer for processing purchase events asynchronously
 * This enables scalable, event-driven processing of ticket purchases
 * Gracefully handles when Kafka is disabled
 */
@Service
public class PurchaseEventConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(PurchaseEventConsumer.class);
    
    @Value("${spring.kafka.enabled:true}")
    private boolean kafkaEnabled;

    /**
     * Processes ticket purchased events from Kafka
     * This method runs asynchronously and can handle high-volume purchases
     */
    @KafkaListener(topics = "ticket-purchased", groupId = "ticketing-group", autoStartup = "${spring.kafka.enabled:true}")
    public void handleTicketPurchased(TicketPurchasedEvent event) {
        if (!kafkaEnabled) {
            logger.info("Kafka disabled - skipping ticket purchase processing: {}", event.getTicketId());
            return;
        }
        
        try {
            logger.info("Processing ticket purchase event: {}", event.getTicketId());
            
            // TODO: Add your business logic here for scalable processing
            // Examples:
            // - Send confirmation emails
            // - Update analytics
            // - Trigger notifications
            // - Process loyalty points
            // - Update user preferences
            
            processTicketPurchase(event);
            
            logger.info("Successfully processed ticket purchase: {}", event.getTicketId());
            
        } catch (Exception e) {
            logger.error("Error processing ticket purchase event: {}", e.getMessage(), e);
            // TODO: Implement dead letter queue or retry mechanism
        }
    }

    /**
     * Business logic for processing ticket purchases
     * This can be extended for various use cases
     */
    private void processTicketPurchase(TicketPurchasedEvent event) {
        // Example business logic:
        
        // 1. Send confirmation email
        sendConfirmationEmail(event);
        
        // 2. Update analytics
        updatePurchaseAnalytics(event);
        
        // 3. Process loyalty points
        processLoyaltyPoints(event);
        
        // 4. Update user preferences
        updateUserPreferences(event);
    }

    private void sendConfirmationEmail(TicketPurchasedEvent event) {
        // TODO: Implement email service
        logger.info("Sending confirmation email for ticket: {}", event.getTicketId());
    }

    private void updatePurchaseAnalytics(TicketPurchasedEvent event) {
        // TODO: Implement analytics service
        logger.info("Updating analytics for ticket: {}", event.getTicketId());
    }

    private void processLoyaltyPoints(TicketPurchasedEvent event) {
        // TODO: Implement loyalty service
        logger.info("Processing loyalty points for ticket: {}", event.getTicketId());
    }

    private void updateUserPreferences(TicketPurchasedEvent event) {
        // TODO: Implement recommendation service
        logger.info("Updating user preferences for ticket: {}", event.getTicketId());
    }
} 