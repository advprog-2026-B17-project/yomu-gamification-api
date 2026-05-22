# Yomu Gamification API

Spring Boot service for gamification features including achievements, missions, clans, and seasons.

## Tech Stack

- Java 17
- Spring Boot 3.2.5
- PostgreSQL with Flyway migrations
- RabbitMQ for event consumption

## Building

```bash
mvn clean package
```

## Running

```bash
java -jar target/yomu-gamification-api-1.0.0.jar
```

## Configuration

Environment variables:
- `DB_HOST` - PostgreSQL host (default: localhost)
- `DB_PORT` - PostgreSQL port (default: 5432)
- `DB_NAME` - Database name (default: yomu_gamification)
- `DB_USERNAME` - Database user (default: postgres)
- `DB_PASSWORD` - Database password (default: postgres)
- `RABBITMQ_HOST` - RabbitMQ host (default: localhost)
- `RABBITMQ_PORT` - RabbitMQ port (default: 5672)
- `RABBITMQ_USERNAME` - RabbitMQ user (default: guest)
- `RABBITMQ_PASSWORD` - RabbitMQ password (default: guest)
- `GATEWAY_SHARED_SECRET` - Secret for inter-service authentication