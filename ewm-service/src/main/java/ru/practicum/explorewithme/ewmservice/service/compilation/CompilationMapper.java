package ru.practicum.explorewithme.ewmservice.service.compilation;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.ewmservice.model.Compilation;
import ru.practicum.explorewithme.ewmservice.model.Event;

import java.util.List;

@UtilityClass
public class CompilationMapper {
    public static CompilationDto toCompilationDto(final Compilation compilation, final List<EventShortDto> events) {
        return new CompilationDto(compilation.getId(), compilation.getTitle(), compilation.isPinned(), events);
    }

    public static Compilation toCompilation(final CompilationDto compilationDto, final List<Event> events) {
        return new Compilation(compilationDto.getId(), compilationDto.getTitle(), compilationDto.isPinned(), events);
    }

    public static Compilation toCompilation(final NewCompilationDto newCompilationDto, final List<Event> events) {
        return new Compilation(null, newCompilationDto.getTitle(), newCompilationDto.isPinned(), events);
    }
}
