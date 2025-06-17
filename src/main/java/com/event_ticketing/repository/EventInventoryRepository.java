package com.event_ticketing.repository;

import com.event_ticketing.entity.EventInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventInventoryRepository extends JpaRepository<EventInventory, Long> {
    Optional<EventInventory> findByEventId(Long eventId);
    
    @Modifying
    @Query("UPDATE EventInventory ei SET ei.availableTickets = ei.availableTickets - 1 WHERE ei.eventId = :eventId AND ei.availableTickets > 0")
    int decrementAvailableTickets(@Param("eventId") Long eventId);
    
    @Modifying
    @Query("UPDATE EventInventory ei SET ei.availableTickets = ei.availableTickets + 1 WHERE ei.eventId = :eventId")
    int incrementAvailableTickets(@Param("eventId") Long eventId);
} 