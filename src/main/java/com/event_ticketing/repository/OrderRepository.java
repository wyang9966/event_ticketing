package com.event_ticketing.repository;

import com.event_ticketing.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByEventId(Long eventId);
    List<Order> findByUserIdAndEventId(Long userId, Long eventId);
}
