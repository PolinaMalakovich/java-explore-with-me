package ru.practicum.explorewithme.ewmservice.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestStatus;
import ru.practicum.explorewithme.ewmservice.exception.EntityNotFoundException;
import ru.practicum.explorewithme.ewmservice.model.Compilation;
import ru.practicum.explorewithme.ewmservice.repository.CompilationRepository;
import ru.practicum.explorewithme.ewmservice.repository.EventRepository;
import ru.practicum.explorewithme.ewmservice.repository.ParticipationRequestRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.ewmservice.service.compilation.CompilationMapper.toCompilation;
import static ru.practicum.explorewithme.ewmservice.service.compilation.CompilationMapper.toCompilationDto;
import static ru.practicum.explorewithme.ewmservice.service.event.EventMapper.toEventShortDto;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    @Override
    public List<CompilationDto> getCompilations(final boolean pinned, final int from, final int size) {
        return compilationRepository
            .findByPinned(pinned, PageRequest.of(from / size, size))
            .map(compilation -> toCompilationDto(
                    compilation,
                    eventRepository
                        .findEventsByCompilationId(compilation.getId())
                        .map(event -> toEventShortDto(
                            event,
                            participationRequestRepository
                                .findByEventIdAndStatus(event.getId(), ParticipationRequestStatus.APPROVED)
                                .count(),
                            null)
                        )
                        .collect(Collectors.toSet())
                )
            )
            .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilation(final long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId)
            .orElseThrow(() -> new EntityNotFoundException("Compilation", compilationId));
        Set<EventShortDto> events = eventRepository
            .findEventsByCompilationId(compilation.getId())
            .map(event -> toEventShortDto(
                event,
                participationRequestRepository
                    .findByEventIdAndStatus(event.getId(), ParticipationRequestStatus.APPROVED)
                    .count(),
                null)
            )
            .collect(Collectors.toSet());

        return toCompilationDto(compilation, events);
    }

    @Override
    @Transactional
    public CompilationDto addCompilation(final NewCompilationDto newCompilationDto) {
        final Compilation compilation = toCompilation(newCompilationDto);
        final Compilation newCompilation = compilationRepository.save(compilation);

        return null;
    }

    @Override
    @Transactional
    public void deleteCompilation(long compilationId) {
        compilationRepository.deleteById(compilationId);
        log.info("Compilation " + compilationId + " deleted successfully.");
    }

    @Override
    @Transactional
    public void deleteEventFromCompilation(long compilationId, long eventId) {

    }

    @Override
    @Transactional
    public CompilationDto addEventToCompilation(long compilationId, long eventId) {
        return null;
    }

    @Override
    @Transactional
    public void unpinCompilation(long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId)
            .orElseThrow(() -> new EntityNotFoundException("Compilation", compilationId));
        compilation.setPinned(false);
        log.info("Compilation " + compilationId + " unpinned successfully.");
    }

    @Override
    @Transactional
    public void pinCompilation(long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId)
            .orElseThrow(() -> new EntityNotFoundException("Compilation", compilationId));
        compilation.setPinned(true);
        log.info("Compilation " + compilationId + " pinned successfully.");
    }
}
