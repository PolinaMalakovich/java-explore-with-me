package ru.practicum.explorewithme.dto.compilation;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Value
public class NewCompilationDto {
    @NotBlank
    String title;
    Boolean pinned;
    Set<Long> events;
}
