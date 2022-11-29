package ru.practicum.explorewithme.ewmservice.service.participationrequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.event.EventState;
import ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestDto;
import ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestStatus;
import ru.practicum.explorewithme.ewmservice.exception.EntityNotFoundException;
import ru.practicum.explorewithme.ewmservice.exception.ForbiddenException;
import ru.practicum.explorewithme.ewmservice.exception.PermissionDeniedException;
import ru.practicum.explorewithme.ewmservice.exception.event.EventException;
import ru.practicum.explorewithme.ewmservice.exception.event.EventModerationException;
import ru.practicum.explorewithme.ewmservice.model.Event;
import ru.practicum.explorewithme.ewmservice.model.ParticipationRequest;
import ru.practicum.explorewithme.ewmservice.model.User;
import ru.practicum.explorewithme.ewmservice.repository.EventRepository;
import ru.practicum.explorewithme.ewmservice.repository.UserRepository;
import ru.practicum.explorewithme.ewmservice.repository.participationrequest.ParticipationRequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestStatus.*;
import static ru.practicum.explorewithme.ewmservice.service.participationrequest.ParticipationRequestMapper.toParticipationRequestDto;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository participationRequestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getParticipationRequests(final long userId) {
        return participationRequestRepository
            .findByRequesterId(userId)
            .map(ParticipationRequestMapper::toParticipationRequestDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto addParticipationRequest(final long userId, final long eventId) {
        if (participationRequestRepository.findByEventIdAndRequesterId(eventId, userId).isPresent()) {
            throw new ForbiddenException(userId, eventId, "event", "add a participation request to",
                "This request already exists.");
        }
        final Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event", eventId));
        if (Objects.equals(userId, event.getInitiator().getId())) {
            throw new ForbiddenException(userId, eventId, "event", "add a participation request to",
                "Event initiator cannot add a participation request to their own event.");
        }
        if (!Objects.equals(event.getState(), EventState.PUBLISHED)) {
            throw new ForbiddenException(userId, eventId, "event", "add a participation request to",
                "This event has not been published yet.");
        }
        if (participationRequestRepository.findByEventIdAndStatus(event.getId(), CONFIRMED).count()
            == event.getParticipantLimit()) {
            throw new UnsupportedOperationException();
        }
        ParticipationRequestStatus status = PENDING;
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            status = CONFIRMED;
        }
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User", userId));
        final ParticipationRequest participationRequest = new ParticipationRequest(
            null,
            LocalDateTime.now(),
            user,
            event,
            status
        );
        final ParticipationRequest newParticipationRequest = participationRequestRepository.save(participationRequest);
        log.info("New participation request created successfully.");

        return toParticipationRequestDto(newParticipationRequest);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelParticipationRequest(final long userId, final long requestId) {
        final ParticipationRequest participationRequest = participationRequestRepository
            .findByIdAndRequesterId(requestId, userId)
            .orElseThrow(() -> new EntityNotFoundException("Participation request", requestId));
        participationRequest.setStatus(CANCELED);
        log.info("Participation request " + participationRequest.getId() + " canceled successfully.");

        return toParticipationRequestDto(participationRequest);
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestsForEvent(final long userId, final long eventId) {
        return participationRequestRepository
            .findParticipationRequestsByInitiatorIdAndEventId(userId, eventId)
            .map(ParticipationRequestMapper::toParticipationRequestDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto approveParticipationRequest(final long userId,
                                                               final long eventId,
                                                               final long requestId) {
        final ParticipationRequest participationRequest = participationRequestRepository.findById(requestId)
            .orElseThrow(() -> new EntityNotFoundException("Participation request", requestId));
        final Event event = participationRequest.getEvent();
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            throw new EventModerationException(requestId, event.getId());
        }
        checkInitiatorAndEvent(event, userId, eventId, requestId, "approve");
        long count = participationRequestRepository.findByEventIdAndStatus(event.getId(), CONFIRMED).count();
        if (count < event.getParticipantLimit()) {
            participationRequest.setStatus(CONFIRMED);
            count += 1;
        }
        if (count == event.getParticipantLimit()) {
            participationRequestRepository
                .findByEventIdAndStatus(event.getId(), PENDING)
                .forEach(pr -> pr.setStatus(REJECTED));
        }
        log.info("Participation request " + participationRequest.getId() + " approved successfully.");

        return toParticipationRequestDto(participationRequest);
    }

    @Override
    @Transactional
    public ParticipationRequestDto declineParticipationRequest(final long userId,
                                                               final long eventId,
                                                               final long requestId) {
        final ParticipationRequest participationRequest = participationRequestRepository.findById(requestId)
            .orElseThrow(() -> new EntityNotFoundException("Participation request", requestId));
        final Event event = participationRequest.getEvent();
        checkInitiatorAndEvent(event, userId, eventId, requestId, "reject");
        participationRequest.setStatus(REJECTED);
        log.info("Participation request " + participationRequest.getId() + " rejected successfully.");

        return toParticipationRequestDto(participationRequest);
    }

    private void checkInitiatorAndEvent(final Event event,
                                        final long userId,
                                        final long eventId,
                                        final long requestId,
                                        final String action) {
        if (event.getInitiator().getId() != userId) {
            throw new PermissionDeniedException(userId, eventId, "event", action + " participation requests for",
                "Only event initiator can " + action + " requests.");
        }
        if (event.getId() != eventId) {
            throw new EventException(
                "Participation request " + requestId + " contains event " + event.getId() +
                    ". It does not match the event sent: " + eventId + "."
            );
        }
    }
}
