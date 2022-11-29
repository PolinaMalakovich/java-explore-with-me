package ru.practicum.explorewithme.ewmservice.service.participationrequest;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestDto;
import ru.practicum.explorewithme.ewmservice.model.Event;
import ru.practicum.explorewithme.ewmservice.model.ParticipationRequest;
import ru.practicum.explorewithme.ewmservice.model.User;

@UtilityClass
public class ParticipationRequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(final ParticipationRequest participationRequest) {
        return new ParticipationRequestDto(
            participationRequest.getId(),
            participationRequest.getRequester().getId(),
            participationRequest.getEvent().getId(),
            participationRequest.getCreated(), participationRequest.getStatus()
        );
    }

    public static ParticipationRequest toParticipationRequest(final ParticipationRequestDto participationRequestDto,
                                                              final User requester,
                                                              final Event event) {
        return new ParticipationRequest(
            participationRequestDto.getId(),
            participationRequestDto.getCreated(),
            requester,
            event,
            participationRequestDto.getStatus()
        );
    }
}
