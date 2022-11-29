package ru.practicum.explorewithme.ewmservice.service.compilation;

import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilation(long compilationId);

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(long compilationId);

    void deleteEventFromCompilation(long compilationId, long eventId);

    CompilationDto addEventToCompilation(long compilationId, long eventId);

    void unpinCompilation(long compilationId);

    void pinCompilation(long compilationId);
}
