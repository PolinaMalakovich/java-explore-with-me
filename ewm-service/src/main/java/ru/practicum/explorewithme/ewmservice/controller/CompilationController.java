package ru.practicum.explorewithme.ewmservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.ewmservice.service.compilation.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                                @RequestParam(defaultValue = "10") @PositiveOrZero final int size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable(name = "compId") @PositiveOrZero long compilationId) {
        return compilationService.getCompilation(compilationId);
    }

    @PostMapping("/admin/compilations")
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void deleteCompilation(@PathVariable(name = "compId") @PositiveOrZero long compilationId) {
        compilationService.deleteCompilation(compilationId);
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable(name = "compId") @PositiveOrZero long compilationId,
                                           @PathVariable @PositiveOrZero long eventId) {
        compilationService.deleteEventFromCompilation(compilationId, eventId);
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public CompilationDto addEventToCompilation(@PathVariable(name = "compId") @PositiveOrZero long compilationId,
                                                @PathVariable @PositiveOrZero long eventId) {
        return compilationService.addEventToCompilation(compilationId, eventId);
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public void unpinCompilation(@PathVariable(name = "compId") @PositiveOrZero long compilationId) {
        compilationService.unpinCompilation(compilationId);
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public void pinCompilation(@PathVariable(name = "compId") @PositiveOrZero long compilationId) {
        compilationService.pinCompilation(compilationId);
    }
}
