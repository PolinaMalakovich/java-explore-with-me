package ru.practicum.explorewithme.ewmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.ewmservice.model.Event;

import java.util.stream.Stream;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    String EVENTS_COLUMNS = "e.event_id,\n" +
        "e.title,\n" +
        "e.annotation,\n" +
        "e.category,\n" +
        "e.description,\n" +
        "e.created,\n" +
        "e.published,\n" +
        "e.event_date,\n" +
        "e.lat,\n" +
        "e.lon,\n" +
        "e.paid,\n" +
        "e.initiator,\n" +
        "e.participant_limit,\n" +
        "e.request_moderation,\n" +
        "e.state";

    @Query(value = "SELECT " + EVENTS_COLUMNS + " FROM events AS e " +
        "LEFT JOIN event_compilations AS ec ON ec.event_id = e.event_id " +
        "WHERE ec.compilation_id = ?", nativeQuery = true)
    Stream<Event> findEventsByCompilationId(long compilationId);
}
