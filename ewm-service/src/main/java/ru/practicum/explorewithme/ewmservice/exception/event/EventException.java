package ru.practicum.explorewithme.ewmservice.exception.event;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class EventException extends RuntimeException {
    public EventException(String message) {
        super(message);
    }
}
