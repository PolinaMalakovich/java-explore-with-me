package ru.practicum.explorewithme.statsserver.service;

import ru.practicum.explorewithme.dto.hit.HitDto;
import ru.practicum.explorewithme.dto.hit.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    HitDto saveHit(HitDto hitDto);

    List<Stats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
