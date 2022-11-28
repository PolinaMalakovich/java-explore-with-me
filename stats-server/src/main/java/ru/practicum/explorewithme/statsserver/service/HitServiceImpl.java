package ru.practicum.explorewithme.statsserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.hit.HitDto;
import ru.practicum.explorewithme.dto.hit.Stats;
import ru.practicum.explorewithme.statsserver.model.App;
import ru.practicum.explorewithme.statsserver.model.Hit;
import ru.practicum.explorewithme.statsserver.repository.AppRepository;
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
    private final AppRepository appRepository;

    @Override
    @Transactional
    public HitDto saveHit(final HitDto hitDto) {
        final App app = appRepository.findByName(hitDto.getApp())
            .orElseGet(() -> appRepository.save(new App(null, hitDto.getApp())));
        final Hit hit = toHit(hitDto, app);
        final Hit newHit = hitRepository.save(hit);
        log.info("New hit saved successfully.");

        return toHitDto(newHit);
    }

    @Override
    public List<Stats> getStats(final LocalDateTime start,
                                final LocalDateTime end,
                                final List<String> uris,
                                final boolean unique) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date cannot be earlier than start date.");
        }
        List<Stats> statsList;
        if (unique) {
            statsList = hitRepository.getAllUnique(start, end, uris);
        } else {
            statsList = hitRepository.getAll(start, end, uris);
        }

        return statsList;
    }
}
