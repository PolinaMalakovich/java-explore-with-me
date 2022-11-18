package ru.practicum.explorewithme.dto.event;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Value
public class NewEventDto {
    @NotBlank
    @Size(min = 3, max = 120, message = "Title should be shorter than 120 and longer than 3 characters.")
    String title;
    @NotBlank
    @Size(min = 20, max = 2000, message = "Annotation should be shorter than 2000 and longer than 20 characters.")
    String annotation;
    long category;
    @NotBlank
    @Size(min = 20, max = 7000, message = "Description should be shorter than 7000 and longer than 20 characters.")
    String description;
    @NotNull
    LocalDateTime eventDate;
    @NotNull
    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
}
