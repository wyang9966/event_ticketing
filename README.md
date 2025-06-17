# Real-Time Event Ticketing System

A scalable event ticketing system built with Spring Boot, featuring real-time processing, Kafka event streaming, and Kubernetes deployment.

## Features

- **Event Management**: Create and manage events with inventory tracking
- **Ticket Purchasing**: Real-time ticket purchasing with quantity support
- **Order Management**: Complete order lifecycle management
- **User Management**: User registration and authentication
- **Event Streaming**: Kafka-based event-driven architecture
- **Scalable Architecture**: Microservices-ready with event sourcing
- **Payment Integration**: Stripe payment processing (TODO)
- **AI Recommendations**: Spring AI integration (TODO)

## Prerequisites

- Docker & Docker Compose
- Git Bash (for Windows users)

## Quick Start

### 1. Clone and Navigate to Project
```bash
cd event_ticketing
```

### 2. Start the Complete Stack
```bash
docker compose -f docker-compose-full.yml up -d
```

This will start:
- **MySQL Database** (port 3307)
- **Kafka** (port 9092) 
- **Zookeeper** (port 2181)
- **Kafka UI** (port 8081)
- **Spring Boot App** (port 8080)

### 3. Verify Everything is Running
```bash
docker ps
```

### 4. Test the API
```bash
# Create a user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com"
  }'

# Create an event
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Summer Concert 2024",
    "description": "Live outdoor music event",
    "date": "2024-07-15T19:00:00",
    "venue": "Central Park",
    "totalTickets": 100,
    "price": 25.00
  }'

# Purchase tickets
curl -X POST http://localhost:8080/api/tickets/purchase \
  -H "Content-Type: application/json" \
  -d '{
    "eventId": 1,
    "userId": 1,
    "quantity": 2
  }'
```

## API Endpoints

### Users
- `POST /api/users` - Create user
- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/email/{email}` - Get user by email

### Events
- `POST /api/events` - Create new event
- `GET /api/events` - List all events
- `GET /api/events/{id}` - Get event details
- `GET /api/events/recommendations/{userId}` - Get recommended events

### Tickets
- `POST /api/tickets/purchase` - Purchase tickets
- `GET /api/tickets/event/{eventId}` - Get tickets by event
- `GET /api/tickets/user/{userId}` - Get tickets by user
- `GET /api/tickets/event/{eventId}/count` - Get sold tickets count
- `GET /api/tickets/event/{eventId}/available` - Get available tickets count

### Orders
- `GET /api/orders` - List all orders
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders/user/{userId}` - Get orders by user
- `GET /api/orders/event/{eventId}` - Get orders by event

## Monitoring

### Kafka UI
- **URL**: http://localhost:8081
- **Features**: Monitor topics, messages, consumer groups
- **Topics**: `event-created`, `ticket-purchased`, `order-created`

### Application Logs
```bash
# View app logs
docker compose -f docker-compose-full.yml logs app

# Follow logs in real-time
docker compose -f docker-compose-full.yml logs -f app

# View all logs
docker compose -f docker-compose-full.yml logs
```

### Database Access
```bash
# Connect to MySQL container
docker exec -it event-ticketing-mysql mysql -u ticketing_user -p1232384 event_ticketing_db

# View tables
SHOW TABLES;
SELECT * FROM users;
SELECT * FROM events;
SELECT * FROM tickets;
SELECT * FROM orders;
```

## Docker Commands

### Start Services
```bash
docker compose -f docker-compose-full.yml up -d
```

### Stop Services
```bash
docker compose -f docker-compose-full.yml down
```

### Rebuild Application
```bash
docker compose -f docker-compose-full.yml up -d --build app
```

### View Container Status
```bash
docker ps
```

## Architecture

### Event-Driven Design
1. **Event Creation**: Publishes to `event-created` topic
2. **Ticket Purchase**: Publishes to `ticket-purchased` topic
3. **Order Processing**: Asynchronous order creation
4. **Scalable Processing**: Multiple consumers handle high load

### Database Schema
- **Users**: User accounts and preferences
- **Events**: Event information and metadata
- **EventInventory**: Available tickets per event
- **Tickets**: Individual ticket records
- **Orders**: Purchase orders with multiple tickets

### Technology Stack
- **Backend**: Spring Boot 3.x with Java 21
- **Database**: MySQL 8.0
- **Event Streaming**: Apache Kafka
- **Containerization**: Docker & Docker Compose
- **Orchestration**: Kubernetes (k8s/ directory)

## Development

### Project Structure
```
src/main/java/com/event_ticketing/
├── controller/     # REST API controllers
├── service/        # Business logic services
├── repository/     # Data access layer
├── entity/         # JPA entities
├── dto/           # Data transfer objects
├── event/         # Kafka events
├── config/        # Configuration classes
└── exception/     # Exception handling
```

### Adding New Features
1. **Create Entity**: Add JPA entity in `entity/` package
2. **Add Repository**: Create repository interface
3. **Implement Service**: Add business logic in service layer
4. **Create Controller**: Add REST endpoints
5. **Add Events**: Create Kafka events for async processing

## Production Deployment

### Kubernetes
```bash
kubectl apply -f k8s/
```

### Environment Variables
Set these for production:
- `SPRING_DATASOURCE_URL`
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`
- `STRIPE_SECRET_KEY`
- `SPRING_AI_OPENAI_API_KEY`

## Troubleshooting

### Common Issues

**Port Conflicts**
- MySQL: Uses port 3307 (Docker) vs 3306 (local)
- Kafka: Port 9092
- App: Port 8080
- Kafka UI: Port 8081

**Container Issues**
```bash
# Check container status
docker ps

# View logs
docker compose -f docker-compose-full.yml logs app

# Restart services
docker compose -f docker-compose-full.yml restart
```

**Database Issues**
```bash
# Reset database
docker compose -f docker-compose-full.yml down -v
docker compose -f docker-compose-full.yml up -d
```

## TODO

### Payment Integration
- [ ] Implement Stripe payment processing
  - [ ] Add Stripe SDK dependencies to pom.xml
  - [ ] Configure Stripe API keys in application.properties
  - [ ] Create PaymentService for Stripe integration
  - [ ] Implement payment intent creation
  - [ ] Add webhook handling for payment confirmations
  - [ ] Integrate payment flow with ticket purchase
  - [ ] Add payment status tracking
  - [ ] Implement refund functionality

### AI-Powered Features
- [ ] Add Spring AI recommendations
  - [ ] Add Spring AI dependencies to pom.xml
  - [ ] Configure OpenAI API keys in application.properties
  - [ ] Implement user preference analysis
  - [ ] Add event similarity scoring
  - [ ] Create personalized recommendation algorithm
  - [ ] Add recommendation caching for performance
  - [ ] Implement event content generation
  - [ ] Add AI-powered event descriptions

### Production Deployment
- [ ] Complete Kubernetes production setup
  - [ ] Add MySQL StatefulSet and PersistentVolume
  - [ ] Add Kafka StatefulSet with proper storage
  - [ ] Add Zookeeper StatefulSet
  - [ ] Create Kafka UI deployment and service
  - [ ] Add ingress controller for external access
  - [ ] Configure persistent storage for all services
  - [ ] Add horizontal pod autoscaling (HPA)
  - [ ] Implement proper resource limits and requests
  - [ ] Add monitoring with Prometheus and Grafana
  - [ ] Set up proper secrets management
  - [ ] Configure backup and disaster recovery

### Core Features
- [ ] Implement email notifications
- [ ] Add user authentication and authorization
- [ ] Create admin dashboard
- [ ] Add analytics and reporting
- [ ] Implement caching with Redis
- [ ] Add monitoring and metrics

### Infrastructure
- [ ] Add comprehensive test coverage
- [ ] Implement CI/CD pipeline
- [ ] Add API documentation with Swagger
- [ ] Implement rate limiting
- [ ] Add security headers and CORS configuration

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the MIT License.