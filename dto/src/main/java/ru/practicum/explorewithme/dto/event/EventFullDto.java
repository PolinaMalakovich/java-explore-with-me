package ru.practicum.explorewithme.dto.event;

import lombok.Value;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
public class EventFullDto {
    Long id;
    @NotBlank
    String title;
    @NotBlank
    String annotation;
    @NotNull
    CategoryDto category;
    String description;
    LocalDateTime created;
    LocalDateTime published;
    @NotNull
    LocalDateTime eventDate;
    @NotNull
    LocationDto location;
    boolean paid;
    @NotNull
    UserShortDto initiator;
    Integer participantLimit;
    Boolean requestModeration;
    EventState state;
    Long views;
}
