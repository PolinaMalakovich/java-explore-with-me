package ru.practicum.explorewithme.ewmservice.service.participationrequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.event.EventState;
import ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestDto;
import ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestStatus;
import ru.practicum.explorewithme.ewmservice.exception.EntityNotFoundException;
import ru.practicum.explorewithme.ewmservice.model.Event;
import ru.practicum.explorewithme.ewmservice.model.ParticipationRequest;
import ru.practicum.explorewithme.ewmservice.model.User;
import ru.practicum.explorewithme.ewmservice.repository.EventRepository;
import ru.practicum.explorewithme.ewmservice.repository.ParticipationRequestRepository;
import ru.practicum.explorewithme.ewmservice.repository.UserRepository;

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
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User", userId));
        final Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event", eventId));
        if (participationRequestRepository.findByEventIdAndRequesterId(event.getId(), user.getId()).isPresent()) {
            throw new UnsupportedOperationException();
        }
        if (Objects.equals(user.getId(), event.getInitiator().getId())) {
            throw new UnsupportedOperationException();
        }
        if (!Objects.equals(event.getState(), EventState.PUBLISHED)) {
            throw new UnsupportedOperationException();
        }
        if (participationRequestRepository.findByEventIdAndStatus(event.getId(), CONFIRMED).count()
            == event.getParticipantLimit()) {
            throw new UnsupportedOperationException();
        }
        ParticipationRequestStatus status = PENDING;
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            status = CONFIRMED;
        }
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
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User", userId));
        final ParticipationRequest participationRequest = participationRequestRepository.findById(requestId)
            .orElseThrow(() -> new EntityNotFoundException("Participation request", requestId));
        if (!Objects.equals(user.getId(), participationRequest.getRequester().getId())) {
            throw new UnsupportedOperationException();
        }
        participationRequest.setStatus(CANCELED);
        log.info("Participation request " + participationRequest.getId() + " canceled successfully.");

        return toParticipationRequestDto(participationRequest);
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestsForEvent(final long userId, final long eventId) {
        checkInitiator(userId, eventId);
        return participationRequestRepository
            .findByEventId(eventId)
            .map(ParticipationRequestMapper::toParticipationRequestDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto approveParticipationRequest(final long userId,
                                                               final long eventId,
                                                               final long requestId) {
        checkInitiator(userId, eventId);
        final Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event", eventId));
        final ParticipationRequest participationRequest = participationRequestRepository.findById(requestId)
            .orElseThrow(() -> new EntityNotFoundException("Participation request", requestId));
        if (participationRequestRepository.findByEventIdAndStatus(event.getId(), CONFIRMED).count()
            < event.getParticipantLimit()) {
            participationRequest.setStatus(CONFIRMED);
        }
        if (participationRequestRepository.findByEventIdAndStatus(event.getId(), CONFIRMED).count()
            == event.getParticipantLimit()) {
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
        checkInitiator(userId, eventId);
        final ParticipationRequest participationRequest = participationRequestRepository.findById(requestId)
            .orElseThrow(() -> new EntityNotFoundException("Participation request", requestId));
        participationRequest.setStatus(REJECTED);
        log.info("Participation request " + participationRequest.getId() + " rejected successfully.");

        return toParticipationRequestDto(participationRequest);
    }

    private void checkInitiator(long userId, long eventId) {
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User", userId));
        final Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event", eventId));
        if (!Objects.equals(user.getId(), event.getInitiator().getId())) {
            throw new UnsupportedOperationException();
        }
    }
}
