package ru.practicum.explorewithme.ewmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestStatus;
import ru.practicum.explorewithme.ewmservice.model.ParticipationRequest;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    Stream<ParticipationRequest> findByRequesterId(long requesterId);

    Stream<ParticipationRequest> findByEventId(long eventId);

    Stream<ParticipationRequest> findByEventIdAndStatus(long eventId, ParticipationRequestStatus status);

    Optional<ParticipationRequest> findByEventIdAndRequesterId(long eventId, long requesterId);
}
