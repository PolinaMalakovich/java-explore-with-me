package ru.practicum.explorewithme.ewmservice.exception.event;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class EventDateException extends RuntimeException {
    String hours;
    String action;

    public EventDateException(final String hours, final String action) {
        super("Event should start at least " + hours + " hours after being " + action + ".");
        this.hours = hours;
        this.action = action;
    }
}
