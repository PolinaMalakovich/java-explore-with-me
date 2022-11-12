package ru.practicum.explorewithme.dto.participationrequest;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ParticipationRequestDto {
    Long id;
    Long requester;
    Long event;
    LocalDateTime created;
    ParticipationRequestStatus status;
}
