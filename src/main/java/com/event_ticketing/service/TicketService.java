package com.event_ticketing.service;

import com.event_ticketing.domain.TicketStatus;
import com.event_ticketing.dto.PurchaseTicketRequest;
import com.event_ticketing.entity.Event;
import com.event_ticketing.entity.EventInventory;
import com.event_ticketing.entity.Order;
import com.event_ticketing.entity.Ticket;
import com.event_ticketing.event.TicketPurchasedEvent;
import com.event_ticketing.repository.EventInventoryRepository;
import com.event_ticketing.repository.EventRepository;
import com.event_ticketing.repository.OrderRepository;
import com.event_ticketing.repository.TicketRepository;
import com.event_ticketing.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventInventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final KafkaEventPublisher kafkaEventPublisher;

    public TicketService(TicketRepository ticketRepository, EventRepository eventRepository,
                        UserRepository userRepository, EventInventoryRepository inventoryRepository,
                        OrderRepository orderRepository, PaymentService paymentService, 
                        KafkaEventPublisher kafkaEventPublisher) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.inventoryRepository = inventoryRepository;
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
        this.kafkaEventPublisher = kafkaEventPublisher;
    }

    @Transactional
    public List<Ticket> purchaseTickets(PurchaseTicketRequest request) {
        // Validate event exists
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Validate user exists
        userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check inventory
        EventInventory inventory = inventoryRepository.findByEventId(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event inventory not found"));

        if (inventory.getAvailableTickets() < request.getQuantity()) {
            throw new RuntimeException("Insufficient tickets available");
        }

        // Process payment
        String paymentId = paymentService.processPayment(request.getUserId(), 
                calculateTotalAmount(request.getQuantity()));

        // Create order
        Order order = createOrder(request.getEventId(), request.getUserId(), 
                request.getQuantity(), paymentId);

        // Create tickets
        List<Ticket> tickets = createTickets(request.getEventId(), request.getUserId(), 
                request.getQuantity(), paymentId);

        // Update inventory
        for (int i = 0; i < request.getQuantity(); i++) {
            inventory.reduceInventory();
        }
        inventoryRepository.save(inventory);

        // Publish event
        tickets.forEach(ticket -> {
            TicketPurchasedEvent event1 = new TicketPurchasedEvent(
                    ticket.getId(), ticket.getEventId(), ticket.getUserId(),
                    ticket.getStatus(), LocalDateTime.now(), calculateTotalAmount(1), paymentId);
            kafkaEventPublisher.publishTicketPurchased(event1);
        });

        return tickets;
    }

    private Order createOrder(Long eventId, Long userId, Integer quantity, String paymentId) {
        Order order = new Order();
        order.setEventId(eventId);
        order.setUserId(userId);
        order.setPurchaseTime(LocalDateTime.now());
        order.validate();
        return orderRepository.save(order);
    }

    private List<Ticket> createTickets(Long eventId, Long userId, Integer quantity, String paymentId) {
        return java.util.stream.IntStream.range(0, quantity)
                .mapToObj(i -> {
                    Ticket ticket = new Ticket();
                    ticket.purchase(eventId, userId);
                    return ticketRepository.save(ticket);
                })
                .collect(Collectors.toList());
    }

    private Double calculateTotalAmount(Integer quantity) {
        // TODO: Get price from event configuration
        return quantity * 50.0; // Default price $50 per ticket
    }

    public List<Ticket> getTicketsByEvent(Long eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    public List<Ticket> getTicketsByUser(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    public long getSoldTicketsCount(Long eventId) {
        return ticketRepository.countByEventId(eventId);
    }

    public long getAvailableTicketsCount(Long eventId) {
        EventInventory inventory = inventoryRepository.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Event inventory not found"));
        return inventory.getAvailableTickets();
    }
} 