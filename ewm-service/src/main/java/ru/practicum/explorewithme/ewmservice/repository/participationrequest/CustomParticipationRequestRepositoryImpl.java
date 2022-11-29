package ru.practicum.explorewithme.ewmservice.repository.participationrequest;

import ru.practicum.explorewithme.ewmservice.model.Event;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class CustomParticipationRequestRepositoryImpl implements CustomParticipationRequestRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Map<Long, Long> getConfirmedRequests(List<Event> eventList) {
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
