package ru.practicum.explorewithme.ewmservice.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class EntityNotFoundException extends RuntimeException {
    String entity;
    long id;

    public EntityNotFoundException(final String entity, final long id) {
        super(entity + " " + id + " does not exist.");
        this.entity = entity;
        this.id = id;
    }
}
