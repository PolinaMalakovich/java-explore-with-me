package ru.practicum.explorewithme.ewmservice.exception.event;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class EventModerationException extends RuntimeException {
    long requestId;
    long eventId;

    public EventModerationException(long requestId, long eventId) {
        super("Request " + requestId + " cannot be approved because event " + eventId +
            " does not require request moderation.");
        this.requestId = requestId;
        this.eventId = eventId;
    }
}
