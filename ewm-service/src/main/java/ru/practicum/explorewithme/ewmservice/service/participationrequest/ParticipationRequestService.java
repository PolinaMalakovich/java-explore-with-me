package ru.practicum.explorewithme.ewmservice.service.participationrequest;

import ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
    List<ParticipationRequestDto> getParticipationRequests(long userId);

    ParticipationRequestDto addParticipationRequest(long userId, long eventId);

    ParticipationRequestDto cancelParticipationRequest(long userId, long requestId);

    List<ParticipationRequestDto> getParticipationRequestsForEvent(long userId, long eventId);

    ParticipationRequestDto approveParticipationRequest(long userId, long eventId, long requestId);

    ParticipationRequestDto declineParticipationRequest(long userId, long eventId, long requestId);
}
