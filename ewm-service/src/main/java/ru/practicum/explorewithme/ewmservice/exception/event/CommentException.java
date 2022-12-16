package ru.practicum.explorewithme.ewmservice.exception.event;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class CommentException extends RuntimeException {
    public CommentException(final String message) {
        super();
    }
}
