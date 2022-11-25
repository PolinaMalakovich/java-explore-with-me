package ru.practicum.explorewithme.ewmservice.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.event.*;
import ru.practicum.explorewithme.ewmservice.EwmClient;
import ru.practicum.explorewithme.ewmservice.exception.EntityNotFoundException;
import ru.practicum.explorewithme.ewmservice.exception.EventDateException;
import ru.practicum.explorewithme.ewmservice.exception.EventStateException;
import ru.practicum.explorewithme.ewmservice.exception.PermissionDeniedException;
import ru.practicum.explorewithme.ewmservice.model.Category;
import ru.practicum.explorewithme.ewmservice.model.Event;
import ru.practicum.explorewithme.ewmservice.model.User;
import ru.practicum.explorewithme.ewmservice.repository.CategoryRepository;
import ru.practicum.explorewithme.ewmservice.repository.EventRepository;
import ru.practicum.explorewithme.ewmservice.repository.ParticipationRequestRepository;
import ru.practicum.explorewithme.ewmservice.repository.UserRepository;
import ru.practicum.explorewithme.ewmservice.spesification.EventSpecifications;
import ru.practicum.explorewithme.util.TreeFunction;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static ru.practicum.explorewithme.dto.event.EventState.*;
import static ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestStatus.CONFIRMED;
import static ru.practicum.explorewithme.ewmservice.service.event.EventMapper.*;
import static ru.practicum.explorewithme.ewmservice.service.event.LocationMapper.toLocation;
import static ru.practicum.explorewithme.ewmservice.spesification.EventSpecifications.*;
import static ru.practicum.explorewithme.ewmservice.util.StatsUtils.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EwmClient ewmClient;
    private final EntityManager entityManager;

    @Override
    public List<EventShortDto> getEvents(final String text,
                                         final List<Long> categories,
                                         final Boolean paid,
                                         final LocalDateTime rangeStart,
                                         final LocalDateTime rangeEnd,
                                         final Boolean onlyAvailable,
                                         final EventSort sort,
                                         final int from,
                                         final int size) {
        final Optional<Specification<Event>> specifications = Stream
            .of(
                Optional.of(published()),
                Optional.ofNullable(text).map(EventSpecifications::textLikeIgnoreCase),
                Optional.ofNullable(categories).map(EventSpecifications::categories),
                Optional.ofNullable(paid).map(EventSpecifications::paid),
                Optional.ofNullable(onlyAvailable).filter(x -> x).map(x -> onlyAvailable()),
                Optional.of(betweenDates(rangeStart, rangeEnd))
            )
            .flatMap(Optional::stream)
            .reduce(Specification::and);

        final PageRequest pageable = PageRequest.of(from / size, size);

        final List<Event> events = specifications
            .map(s -> eventRepository.findAll(s, pageable))
            .orElseGet(() -> eventRepository.findAll(pageable))
            .toList();

        return getEventList(rangeStart, rangeEnd, events, EventMapper::toEventShortDto);
    }

    @Override
    public EventFullDto getEvent(final long eventId) {
        final Event event = eventRepository
            .findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event", eventId));
        if (!event.getState().equals(PUBLISHED)) {
            throw new EventStateException(eventId, "viewed", event.getState().toString(), "Log in to view the event.");
        }

        return getEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getEventsByUser(final long userId, final int from, final int size) {
        final User user =
            userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));
        List<Event> events = eventRepository
            .findEventsByInitiatorIdOrderById(userId, PageRequest.of(from / size, size))
            .collect(toList());
        if (events.isEmpty()) return Collections.emptyList();
        final Map<Event, String> eventUris = getEventUris(events);
        final Map<String, Long> uriStats = getUriStats(ewmClient, events, eventUris.values());
        final Map<Long, Long> confirmedRequests = getConfirmedRequests(events, entityManager);

        return events
            .stream()
            .map(e ->
                toEventShortDto(
                    e,
                    confirmedRequests.getOrDefault(e.getId(), 0L),
                    uriStats.getOrDefault(eventUris.get(e), 0L)
                )
            )
            .collect(toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(final long userId, final UpdateEventRequest updateEventRequest) {
        final Event event = eventRepository.findByIdAndInitiatorId(updateEventRequest.getEventId(), userId)
            .orElseThrow(() -> new EntityNotFoundException("Event", updateEventRequest.getEventId()));
        if (event.getState().equals(PUBLISHED)) {
            throw new EventStateException(event.getId(), "edited", PUBLISHED.toString());
        }
        if (event.getState().equals(CANCELED)) {
            event.setState(PENDING);
        }
        if (updateEventRequest.getEventDate() != null &&
            updateEventRequest.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
            event.setEventDate(updateEventRequest.getEventDate());
        }
        final Event updatedEvent = updateHelper(
            event,
            updateEventRequest.getTitle(),
            updateEventRequest.getAnnotation(),
            updateEventRequest.getCategory(),
            updateEventRequest.getDescription(),
            updateEventRequest.getPaid(),
            updateEventRequest.getParticipantLimit()
        );
        log.info("Event " + updatedEvent.getId() + " updated successfully.");

        return getEventFullDto(updatedEvent);
    }


    @Override
    @Transactional
    public EventFullDto addEvent(final long userId, final NewEventDto newEventDto) {
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User", userId));
        final Category category = categoryRepository.findById(newEventDto.getCategory())
            .orElseThrow(() -> new EntityNotFoundException("Category", newEventDto.getCategory()));
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EventDateException("two", "created");
        }
        final Event event = eventRepository.save(toEvent(user, newEventDto, category));
        log.info("Event created successfully.");

        return toEventFullDto(event, 0L, 0L);
    }

    @Override
    public EventFullDto getEvent(final long userId, final long eventId) {
        final Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
            .orElseThrow(() -> new EntityNotFoundException("Event", eventId));

        return getEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto cancelEvent(final long userId, final long eventId) {
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User", userId));
        final Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event", eventId));
        if (!Objects.equals(user.getId(), event.getInitiator().getId())) {
            throw new PermissionDeniedException(userId, eventId, "event", "cancel");
        }
        if (!event.getState().equals(PENDING)) {
            throw new EventStateException(eventId, "canceled", event.getState().toString());
        }
        event.setState(CANCELED);

        return getEventFullDto(event);
    }

    @Override
    public List<EventFullDto> getEvents(final List<Long> users,
                                        final List<EventState> states,
                                        final List<Long> categories,
                                        final LocalDateTime rangeStart,
                                        final LocalDateTime rangeEnd,
                                        final int from,
                                        final int size) {
        Optional<Specification<Event>> specifications = Stream
            .of(
                Optional.ofNullable(users).map(EventSpecifications::users),
                Optional.ofNullable(states).map(EventSpecifications::states),
                Optional.ofNullable(categories).map(EventSpecifications::categories),
                Optional.of(betweenDates(rangeStart, rangeEnd))
            )
            .flatMap(Optional::stream)
            .reduce(Specification::and);

        PageRequest pageable = PageRequest.of(from / size, size);

        List<Event> events = specifications
            .map(s -> eventRepository.findAll(s, pageable))
            .orElseGet(() -> eventRepository.findAll(pageable))
            .toList();

        return getEventList(rangeStart, rangeEnd, events, EventMapper::toEventFullDto);
    }

    @Override
    @Transactional
    public EventFullDto updateEventForAdmin(final long eventId, final AdminUpdateEventRequest adminUpdate) {
        final Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event", eventId));
        if (adminUpdate.getEventDate() != null) event.setEventDate(adminUpdate.getEventDate());
        if (adminUpdate.getLocation() != null) event.setLocation(toLocation(adminUpdate.getLocation()));
        if (adminUpdate.getRequestModeration() != null) event.setRequestModeration(adminUpdate.getRequestModeration());
        final Event updatedEvent = updateHelper(
            event,
            adminUpdate.getTitle(),
            adminUpdate.getAnnotation(),
            adminUpdate.getCategory(),
            adminUpdate.getDescription(),
            adminUpdate.getPaid(),
            adminUpdate.getParticipantLimit()
        );
        log.info("Event " + updatedEvent.getId() + " updated successfully.");

        return getEventFullDto(updatedEvent);
    }

    @Override
    @Transactional
    public EventFullDto publishEvent(final long eventId) {
        final Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event", eventId));
        if (!event.getState().equals(PENDING)) {
            throw new EventStateException(eventId, "published", event.getState().toString());
        }
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new EventDateException("one", "published");
        }
        event.setState(PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());

        return getEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto rejectEvent(final long eventId) {
        final Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event", eventId));
        if (!event.getState().equals(PENDING)) {
            throw new EventStateException(eventId, "published", event.getState().toString());
        }
        event.setState(CANCELED);

        return getEventFullDto(event);
    }

    private Event updateHelper(final Event event,
                               final String title,
                               final String annotation,
                               final Long categoryId,
                               final String description,
                               final Boolean paid,
                               final Integer participantLimit) {
        if (title != null && !title.isBlank()) event.setTitle(title);
        if (annotation != null && !annotation.isBlank()) event.setAnnotation(annotation);
        if (categoryId != null) {
            Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category", categoryId));
            event.setCategory(category);
        }
        if (description != null && !description.isBlank()) event.setDescription(description);
        if (paid != null) event.setPaid(paid);
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }

        return event;
    }

    private EventFullDto getEventFullDto(final Event event) {
        final long confirmedRequests = requestRepository.findByEventIdAndStatus(event.getId(), CONFIRMED).count();
        String key = "/events/" + event.getId();
        Map<String, Long> uriStats = getUriStats(ewmClient, event.getCreatedOn(), LocalDateTime.now(), List.of(key));

        return toEventFullDto(event, confirmedRequests, uriStats.getOrDefault(key, 0L));
    }

    private <R> List<R> getEventList(final LocalDateTime rangeStart,
                                     final LocalDateTime rangeEnd,
                                     final List<Event> events,
                                     final TreeFunction<Event, Long, Long, R> mapper) {
        if (events.isEmpty()) return Collections.emptyList();

        final LocalDateTime start = checkStart(events, rangeStart);
        final LocalDateTime end = checkEnd(rangeEnd);

        final Map<Event, String> eventUris = getEventUris(events);
        final Map<String, Long> uriStats = getUriStats(ewmClient, start, end, eventUris.values());
        final Map<Long, Long> confirmedRequests = getConfirmedRequests(events, entityManager);

        return events
            .stream()
            .map(e ->
                mapper.apply(
                    e,
                    confirmedRequests.getOrDefault(e.getId(), 0L),
                    uriStats.getOrDefault(eventUris.get(e), 0L)
                )
            )
            .collect(toList());
    }
}
