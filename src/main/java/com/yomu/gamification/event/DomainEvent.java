package com.yomu.gamification.event;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record DomainEvent(
    String eventId,
    String eventType,
    OffsetDateTime occurredAt,
    String producer,
    Map<String, Object> payload
) {}