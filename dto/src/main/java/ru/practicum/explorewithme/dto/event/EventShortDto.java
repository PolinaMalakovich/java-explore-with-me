package ru.practicum.explorewithme.dto.event;

import lombok.Value;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Value
public class EventShortDto {
    Long id;
    String title;
    String annotation;
    CategoryDto category;
    LocalDateTime eventDate;
    UserShortDto initiator;
    boolean paid;
    Long confirmedRequests;
    Long views;
}
