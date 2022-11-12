package ru.practicum.explorewithme.dto.event;

import lombok.Value;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
public class EventShortDto {
    Long id;
    @NotBlank
    String title;
    @NotBlank
    String annotation;
    @NotNull
    CategoryDto category;
    @NotNull
    LocalDateTime eventDate;
    @NotNull
    UserShortDto initiator;
    boolean paid;
    Long confirmedRequests;
    Long views;
}
