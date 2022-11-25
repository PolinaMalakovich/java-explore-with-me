package ru.practicum.explorewithme.ewmservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.ewmservice.model.Event;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    String EVENTS_COLUMNS = "e.event_id,\n" +
        "e.title,\n" +
        "e.annotation,\n" +
        "e.category_id,\n" +
        "e.description,\n" +
        "e.created,\n" +
        "e.published,\n" +
        "e.event_date,\n" +
        "e.lat,\n" +
        "e.lon,\n" +
        "e.paid,\n" +
        "e.initiator_id,\n" +
        "e.participant_limit,\n" +
        "e.request_moderation,\n" +
        "e.state";

    @Query(value = "SELECT " + EVENTS_COLUMNS + " FROM events AS e " +
        "LEFT JOIN event_compilations AS ec ON ec.event_id = e.event_id " +
        "WHERE ec.compilation_id = ? ORDER BY e.event_id", nativeQuery = true)
    Stream<Event> findEventsByCompilationIdOrderById(long compilationId);

    Stream<Event> findEventsByInitiatorIdOrderById(long user, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(long eventId, long initiatorId);
}
