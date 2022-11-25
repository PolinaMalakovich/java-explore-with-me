package ru.practicum.explorewithme.ewmservice.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class PermissionDeniedException extends RuntimeException {
    long userId;
    long entityId;
    String entity;
    String action;

    public PermissionDeniedException(final long userId, final long entityId, final String entity, String action) {
        super("User " + userId + " cannot " + action + " " + entity + " " + entityId + ".");
        this.userId = userId;
        this.entityId = entityId;
        this.entity = entity;
        this.action = action;
    }

    public PermissionDeniedException(final long userId,
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
