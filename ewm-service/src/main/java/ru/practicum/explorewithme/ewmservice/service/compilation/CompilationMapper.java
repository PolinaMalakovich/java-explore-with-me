package ru.practicum.explorewithme.ewmservice.service.compilation;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.ewmservice.model.Compilation;

import java.util.Set;

@UtilityClass
public class CompilationMapper {
    public static CompilationDto toCompilationDto(final Compilation compilation, final Set<EventShortDto> events) {
        return new CompilationDto(compilation.getId(), compilation.getTitle(), compilation.isPinned(), events);
    }

    public static Compilation toCompilation(final CompilationDto compilationDto) {
        return new Compilation(compilationDto.getId(), compilationDto.getTitle(), compilationDto.isPinned());
    }

    public static Compilation toCompilation(final NewCompilationDto newCompilationDto) {
        return new Compilation(null, newCompilationDto.getTitle(), newCompilationDto.getPinned());
    }
}
