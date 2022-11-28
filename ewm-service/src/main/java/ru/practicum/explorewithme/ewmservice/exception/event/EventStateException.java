package ru.practicum.explorewithme.ewmservice.exception.event;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class EventStateException extends RuntimeException {
    long eventId;
    String action;
    String state;

    public EventStateException(final long eventId, final String action, final String state) {
        super("Event " + eventId + " cannot be " + action + " because its current state is " + state + ".");
        this.eventId = eventId;
        this.action = action;
        this.state = state;
    }

    public EventStateException(final long eventId, final String action, final String state, final String info) {
        super("Event " + eventId + " cannot be " + action + " because its current state is " + state + ". " + info);
        this.eventId = eventId;
        this.action = action;
        this.state = state;
    }
}
