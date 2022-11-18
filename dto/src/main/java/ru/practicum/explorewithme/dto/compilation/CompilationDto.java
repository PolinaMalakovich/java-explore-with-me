package ru.practicum.explorewithme.dto.compilation;

import lombok.Value;
import ru.practicum.explorewithme.dto.event.EventShortDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Value
public class CompilationDto {
    long id;
    @NotBlank
    String title;
    boolean pinned;
    List<EventShortDto> events;
}
