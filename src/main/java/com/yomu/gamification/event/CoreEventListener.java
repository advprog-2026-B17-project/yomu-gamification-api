package com.yomu.gamification.event;

import com.yomu.gamification.service.EventConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CoreEventListener {

    private static final Logger log = LoggerFactory.getLogger(CoreEventListener.class);

    private final EventConsumerService consumerService;

    public CoreEventListener(EventConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void handleMessage(Map<String, Object> message) {
        String eventType = (String) message.get("eventType");
        String eventId = (String) message.get("eventId");

        log.info("Received event: {} (id: {})", eventType, eventId);

        if (eventType == null) {
            log.warn("Received message without eventType, skipping");
            return;
        }

        // Idempotency check
        if (consumerService.isEventProcessed(eventId)) {
            log.debug("Event {} already processed, skipping", eventId);
            return;
        }

        try {
            switch (eventType) {
                case "user.created" -> consumerService.handleUserCreated(message);
                case "user.updated" -> consumerService.handleUserUpdated(message);
                case "user.deleted" -> consumerService.handleUserDeleted(message);
                case "reading.created" -> consumerService.handleReadingCreated(message);
                case "reading.updated" -> consumerService.handleReadingUpdated(message);
                case "reading.deleted" -> consumerService.handleReadingDeleted(message);
                case "quiz.completed" -> consumerService.handleQuizCompleted(message);
                case "reading.completed" -> consumerService.handleReadingCompleted(message);
                default -> log.debug("Unhandled event type: {}", eventType);
            }

            consumerService.markEventProcessed(eventId, eventType);
        } catch (Exception e) {
            log.error("Error processing event {}: {}", eventType, e.getMessage(), e);
            throw e; // Rethrow for RabbitMQ to handle retry/dead-letter
        }
    }
}