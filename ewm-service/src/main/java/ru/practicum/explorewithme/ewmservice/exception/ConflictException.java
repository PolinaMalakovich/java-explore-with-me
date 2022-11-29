package ru.practicum.explorewithme.ewmservice.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class ConflictException extends RuntimeException {
    String entity;
    String name;

    public ConflictException(final String entity, final String name) {
        super(entity + " name " + name + " is already taken.");
        this.entity = entity;
        this.name = name;
    }
}
