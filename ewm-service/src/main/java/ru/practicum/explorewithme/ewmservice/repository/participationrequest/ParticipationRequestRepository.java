package ru.practicum.explorewithme.ewmservice.repository.participationrequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestStatus;
import ru.practicum.explorewithme.ewmservice.model.ParticipationRequest;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long>,
    CustomParticipationRequestRepository {
    Stream<ParticipationRequest> findByRequesterId(long requesterId);

    Stream<ParticipationRequest> findByEventId(long eventId);

    Stream<ParticipationRequest> findByEventIdAndStatus(long eventId, ParticipationRequestStatus status);

    Optional<ParticipationRequest> findByEventIdAndRequesterId(long eventId, long requesterId);

    Optional<ParticipationRequest> findByIdAndRequesterId(long requestId, long userId);

    @Query(value = "SELECT p FROM ParticipationRequest p WHERE p.event.initiator.id = ?1 AND p.event.id = ?2")
    Stream<ParticipationRequest> findParticipationRequestsByInitiatorIdAndEventId(long userId, long eventId);
}
