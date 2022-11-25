package ru.practicum.explorewithme.ewmservice.util;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.hit.Stats;
import ru.practicum.explorewithme.ewmservice.EwmClient;
import ru.practicum.explorewithme.ewmservice.model.Event;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

@UtilityClass
public class StatsUtils {
    public static Map<Event, String> getEventUris(final List<Event> events) {
        return events.stream().collect(toMap(identity(), e -> "/events/" + e.getId()));
    }

    public static Map<String, Long> getUriStats(final EwmClient ewmClient,
                                                final LocalDateTime rangeStart,
                                                final LocalDateTime rangeEnd,
                                                final Iterable<String> eventUris) {
        return ewmClient
            .stats(rangeStart, rangeEnd, eventUris, false)
            .stream()
            .collect(toMap(Stats::getUri, Stats::getHits));
    }

    public static Map<String, Long> getUriStats(final EwmClient ewmClient,
                                                final List<Event> events,
                                                final Iterable<String> eventUris) {
        if (events.isEmpty()) return Collections.emptyMap();
        LocalDateTime start = events.stream().map(Event::getCreatedOn).min(Comparator.naturalOrder()).get();

        return ewmClient
            .stats(start, LocalDateTime.now(), eventUris, false)
            .stream()
            .collect(toMap(Stats::getUri, Stats::getHits));
    }

    public static LocalDateTime checkStart(final List<Event> events, final LocalDateTime rangeStart) {
        return rangeStart != null ?
            rangeStart :
            events.stream().map(Event::getCreatedOn).min(Comparator.naturalOrder()).get();
    }

    public static LocalDateTime checkEnd(final LocalDateTime rangeEnd) {
        return rangeEnd != null ? rangeEnd : LocalDateTime.now();
    }

    public static Map<Long, Long> getConfirmedRequests(final List<Event> eventList, final EntityManager entityManager) {
        final String eventIdsPredicate = eventList.isEmpty() ? "" : eventList
            .stream()
            .map(event -> event.getId().toString())
            .collect(joining(", ", "AND pr.event.id IN (", ")"));

        return entityManager.createQuery(
                "SELECT pr.event.id AS eventId, count(pr) AS requestCount " +
                    "FROM ParticipationRequest AS pr " +
                    "WHERE pr.status = 'CONFIRMED' " +
                    eventIdsPredicate +
                    "GROUP BY pr.event.id",
                Tuple.class
            )
            .getResultStream()
            .collect(Collectors.toMap(
                    tuple -> (Long) tuple.get("eventId"),
                    tuple -> (Long) tuple.get("requestCount")
                )
            );
    }
}
