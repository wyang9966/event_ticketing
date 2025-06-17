package com.event_ticketing.repository;

import com.event_ticketing.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByEventId(Long eventId);
    List<Ticket> findByUserId(Long userId);
    List<Ticket> findByEventIdAndStatus(Long eventId, String status);
    
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.eventId = :eventId AND t.status = :status")
    long countByEventIdAndStatus(@Param("eventId") Long eventId, @Param("status") String status);
    
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.eventId = :eventId")
    long countByEventId(@Param("eventId") Long eventId);
} 