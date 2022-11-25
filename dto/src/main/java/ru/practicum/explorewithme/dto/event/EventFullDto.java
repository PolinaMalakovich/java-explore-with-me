package ru.practicum.explorewithme.dto.event;

import lombok.Value;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Value
public class EventFullDto {
    Long id;
    String title;
    String annotation;
    CategoryDto category;
    String description;
    LocalDateTime createdOn;
    LocalDateTime publishedOn;
    LocalDateTime eventDate;
    LocationDto location;
    boolean paid;
    UserShortDto initiator;
    Integer participantLimit;
    Boolean requestModeration;
    EventState state;
    Long confirmedRequests;
    Long views;
}
