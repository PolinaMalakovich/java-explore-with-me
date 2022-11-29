package ru.practicum.explorewithme.ewmservice.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.ewmservice.EwmClient;
import ru.practicum.explorewithme.ewmservice.exception.EntityNotFoundException;
import ru.practicum.explorewithme.ewmservice.model.Compilation;
import ru.practicum.explorewithme.ewmservice.model.Event;
import ru.practicum.explorewithme.ewmservice.repository.CompilationRepository;
import ru.practicum.explorewithme.ewmservice.repository.EventRepository;
import ru.practicum.explorewithme.ewmservice.repository.participationrequest.ParticipationRequestRepository;
import ru.practicum.explorewithme.ewmservice.spesification.CompilationSpecifications;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static ru.practicum.explorewithme.ewmservice.service.compilation.CompilationMapper.toCompilation;
import static ru.practicum.explorewithme.ewmservice.service.compilation.CompilationMapper.toCompilationDto;
import static ru.practicum.explorewithme.ewmservice.service.event.EventMapper.toEventShortDto;
import static ru.practicum.explorewithme.ewmservice.util.PageRequestUtil.getPageRequest;
import static ru.practicum.explorewithme.ewmservice.util.StatsUtils.getEventUris;
import static ru.practicum.explorewithme.ewmservice.util.StatsUtils.getUriStats;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository prRepository;
    private final EwmClient ewmClient;

    @Override
    public List<CompilationDto> getCompilations(final Boolean pinned, final int from, final int size) {
        final Optional<Specification<Compilation>> specifications = Stream
            .of(Optional.ofNullable(pinned).map(CompilationSpecifications::pinned))
            .flatMap(Optional::stream)
            .reduce(Specification::and);

        final PageRequest pageable = getPageRequest(from, size);

        final List<Compilation> compilations = specifications
            .map(s -> compilationRepository.findAll(s, pageable))
            .orElseGet(() -> compilationRepository.findAll(pageable))
            .toList();
        if (compilations.isEmpty()) return Collections.emptyList();
        final List<Event> eventList = compilations
            .stream()
            .flatMap(c -> c.getEvents().stream())
            .distinct()
            .collect(toList());

        final Map<Event, String> eventUris = getEventUris(eventList);
        final Map<String, Long> uriStats = getUriStats(ewmClient, eventList, eventUris.values());
        final Map<Long, Long> confirmedRequests = prRepository.getConfirmedRequests(eventList);

        return compilations
            .stream()
            .map(compilation -> toCompilationDto(
                    compilation,
                    compilation.getEvents().stream()
                        .map(event -> toEventShortDto(
                            event,
                            confirmedRequests.getOrDefault(event.getId(), 0L),
                            uriStats.getOrDefault(eventUris.get(event), 0L))
                        )
                        .collect(toList())
                )
            )
            .collect(toList());
    }

    @Override
    public CompilationDto getCompilation(final long compilationId) {
        final Compilation compilation = compilationRepository.findById(compilationId)
            .orElseThrow(() -> new EntityNotFoundException("Compilation", compilationId));
        final List<Event> eventList = eventRepository
            .findEventsByCompilationIdOrderById(compilation.getId())
            .collect(toList());
        final Map<Event, String> eventUris = getEventUris(eventList);
        final Map<String, Long> uriStats = getUriStats(ewmClient, eventList, eventUris.values());
        final Map<Long, Long> confirmedRequests = prRepository.getConfirmedRequests(eventList);
        final List<EventShortDto> dtoList = eventList
            .stream()
            .map(e ->
                toEventShortDto(
                    e,
                    confirmedRequests.getOrDefault(e.getId(), 0L),
                    uriStats.getOrDefault(eventUris.get(e), 0L)
                )
            )
            .collect(toList());

        return toCompilationDto(compilation, dtoList);
    }

    @Override
    @Transactional
    public CompilationDto addCompilation(final NewCompilationDto newCompilationDto) {
        final List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        final Compilation compilation = toCompilation(newCompilationDto, events);
        final Compilation newCompilation = compilationRepository.save(compilation);
        final Map<Event, String> eventUris = getEventUris(events);
        final Map<String, Long> uriStats = getUriStats(ewmClient, events, eventUris.values());
        final Map<Long, Long> confirmedRequests = prRepository.getConfirmedRequests(events);
        final List<EventShortDto> shorts = events
            .stream()
            .map(event -> toEventShortDto(
                event,
                confirmedRequests.getOrDefault(event.getId(), 0L),
                uriStats.getOrDefault(eventUris.get(event), 0L))
            )
            .collect(Collectors.toList());
        log.info("New compilation created successfully.");

        return toCompilationDto(newCompilation, shorts);
    }

    @Override
    @Transactional
    public void deleteCompilation(final long compilationId) {
        compilationRepository.deleteById(compilationId);
        log.info("Compilation " + compilationId + " deleted successfully.");
    }

    @Override
    @Transactional
    public void deleteEventFromCompilation(final long compilationId, final long eventId) {
        final Compilation compilation = compilationRepository.findById(compilationId)
            .orElseThrow(() -> new EntityNotFoundException("Compilation", compilationId));
        final Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event", eventId));
        compilation.getEvents().remove(event);
        log.info("Event " + eventId + " successfully deleted from compilation " + compilationId + ".");
    }

    @Override
    @Transactional
    public CompilationDto addEventToCompilation(final long compilationId, final long eventId) {
        final Compilation compilation = compilationRepository.findById(compilationId)
            .orElseThrow(() -> new EntityNotFoundException("Compilation", compilationId));
        final Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event", eventId));
        if (compilation.getEvents().contains(event)) {
            log.info("Compilation " + compilationId + " already contains event " + eventId + ".");
        } else {
            compilation.getEvents().add(event);
            log.info("Event " + eventId + " successfully added to compilation " + compilationId + ".");
        }
        final List<Event> eventList = compilation.getEvents();
        final Map<Event, String> eventUris = getEventUris(eventList);
        final Map<String, Long> uriStats = getUriStats(ewmClient, eventList, eventUris.values());
        final Map<Long, Long> confirmedRequests = prRepository.getConfirmedRequests(eventList);
        final List<EventShortDto> events = eventRepository
            .findEventsByCompilationIdOrderById(compilationId)
            .map(e -> toEventShortDto(
                e,
                confirmedRequests.getOrDefault(e.getId(), 0L),
                uriStats.getOrDefault(eventUris.get(event), 0L))
            )
            .collect(Collectors.toList());

        return toCompilationDto(compilation, events);
    }

    @Override
    @Transactional
    public void unpinCompilation(final long compilationId) {
        final Compilation compilation = compilationRepository.findById(compilationId)
            .orElseThrow(() -> new EntityNotFoundException("Compilation", compilationId));
        compilation.setPinned(false);
        log.info("Compilation " + compilationId + " unpinned successfully.");
    }

    @Override
    @Transactional
    public void pinCompilation(final long compilationId) {
        final Compilation compilation = compilationRepository.findById(compilationId)
            .orElseThrow(() -> new EntityNotFoundException("Compilation", compilationId));
        compilation.setPinned(true);
        log.info("Compilation " + compilationId + " pinned successfully.");
    }
}
