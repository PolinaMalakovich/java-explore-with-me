package ru.practicum.explorewithme.dto.event;

import lombok.Value;

import javax.validation.constraints.Future;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Value
public class UpdateEventRequest {
    long eventId;
    @Size(min = 3, max = 120, message = "Title should be shorter than 120 and longer than 3 characters.")
    String title;
    @Size(min = 20, max = 2000, message = "Annotation should be shorter than 2000 and longer than 20 characters.")
    String annotation;
    Long category;
    @Size(min = 20, max = 7000, message = "Description should be shorter than 7000 and longer than 20 characters.")
    String description;
    @Future
    LocalDateTime eventDate;
    Boolean paid;
    @PositiveOrZero
    int participantLimit;
}
