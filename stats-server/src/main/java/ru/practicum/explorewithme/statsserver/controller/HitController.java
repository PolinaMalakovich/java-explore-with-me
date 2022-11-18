package ru.practicum.explorewithme.statsserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.hit.HitDto;
import ru.practicum.explorewithme.dto.hit.Stats;
import ru.practicum.explorewithme.statsserver.service.HitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HitController {
    private final HitService hitService;

    @PostMapping("/hit")
    public HitDto saveHit(@RequestBody final HitDto hitDto) {
        return hitService.saveHit(hitDto);
    }

    @GetMapping("/stats")
    public List<Stats> getStats(@RequestParam final LocalDateTime start,
                                @RequestParam final LocalDateTime end,
                                @RequestParam(required = false) final List<String> uris,
                                @RequestParam(defaultValue = "false") final boolean unique) {
        return hitService.getStats(start, end, uris, unique);
    }
}
