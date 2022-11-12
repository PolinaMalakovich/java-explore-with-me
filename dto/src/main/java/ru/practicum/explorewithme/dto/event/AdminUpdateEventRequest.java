package ru.practicum.explorewithme.dto.event;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class AdminUpdateEventRequest {
    String title;
    String annotation;
    Long category;
    String description;
    LocalDateTime eventDate;
    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
}
