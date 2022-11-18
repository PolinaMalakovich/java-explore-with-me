package ru.practicum.explorewithme.statsserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.hit.HitDto;
import ru.practicum.explorewithme.dto.hit.Stats;
import ru.practicum.explorewithme.statsserver.model.Hit;
import ru.practicum.explorewithme.statsserver.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.statsserver.service.HitMapper.toHit;
import static ru.practicum.explorewithme.statsserver.service.HitMapper.toHitDto;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HitServiceImpl implements HitService {
    private final HitRepository hitRepository;

    @Override
    @Transactional
    public HitDto saveHit(final HitDto hitDto) {
        final Hit hit = toHit(hitDto);
        final Hit newHit = hitRepository.save(hit);
        log.info("New hit saved successfully.");

        return toHitDto(newHit);
    }

    @Override
    public List<Stats> getStats(final LocalDateTime start,
                                final LocalDateTime end,
                                final List<String> uris,
                                final boolean unique) {
        List<Stats> statsList;
        if (unique) {
            statsList = hitRepository.getAllUnique(start, end, uris);
        } else {
            statsList = hitRepository.getAll(start, end, uris);
        }

        return statsList;
    }
}
