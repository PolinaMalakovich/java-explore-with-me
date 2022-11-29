package ru.practicum.explorewithme.ewmservice.controller.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.ewmservice.service.compilation.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
@Validated
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable(name = "compId") @PositiveOrZero long compilationId) {
        compilationService.deleteCompilation(compilationId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable(name = "compId") @PositiveOrZero long compilationId,
                                           @PathVariable @PositiveOrZero long eventId) {
        compilationService.deleteEventFromCompilation(compilationId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public CompilationDto addEventToCompilation(@PathVariable(name = "compId") @PositiveOrZero long compilationId,
                                                @PathVariable @PositiveOrZero long eventId) {
        return compilationService.addEventToCompilation(compilationId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable(name = "compId") @PositiveOrZero long compilationId) {
        compilationService.unpinCompilation(compilationId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable(name = "compId") @PositiveOrZero long compilationId) {
        compilationService.pinCompilation(compilationId);
    }
}
