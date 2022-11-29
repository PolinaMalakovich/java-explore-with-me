package ru.practicum.explorewithme.ewmservice.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class ForbiddenException extends RuntimeException {
    long userId;
    long entityId;
    String entity;
    String action;

    public ForbiddenException(final long userId,
                              final long entityId,
                              final String entity,
                              String action,
                              String reason) {
        super("User " + userId + " cannot " + action + " " + entity + " " + entityId + ". " + reason);
        this.userId = userId;
        this.entityId = entityId;
        this.entity = entity;
        this.action = action;
    }
}
