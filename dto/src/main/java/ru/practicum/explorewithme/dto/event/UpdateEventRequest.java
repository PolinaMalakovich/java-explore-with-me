package ru.practicum.explorewithme.dto.event;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class UpdateEventRequest {
    long eventId;
    String title;
    String annotation;
    Long category;
    String description;
    LocalDateTime eventDate;
    Boolean paid;
    Integer participantLimit;
}
