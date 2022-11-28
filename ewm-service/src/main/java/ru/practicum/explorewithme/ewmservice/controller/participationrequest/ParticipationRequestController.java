package ru.practicum.explorewithme.ewmservice.controller.participationrequest;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestDto;
import ru.practicum.explorewithme.ewmservice.service.participationrequest.ParticipationRequestService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}")
@RequiredArgsConstructor
@Validated
public class ParticipationRequestController {
    private final ParticipationRequestService participationRequestService;

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getParticipationRequests(@PathVariable @PositiveOrZero final long userId) {
        return participationRequestService.getParticipationRequests(userId);
    }

    @PostMapping("/requests")
    public ParticipationRequestDto addParticipationRequest(@PathVariable @PositiveOrZero final long userId,
                                                           @RequestParam @PositiveOrZero final long eventId) {
        return participationRequestService.addParticipationRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable @PositiveOrZero final long userId,
                                                              @PathVariable @PositiveOrZero final long requestId) {
        return participationRequestService.cancelParticipationRequest(userId, requestId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequestsForEvent(
        @PathVariable @PositiveOrZero final long userId,
        @PathVariable @PositiveOrZero final long eventId) {
        return participationRequestService.getParticipationRequestsForEvent(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests/{requestId}/confirm")
    public ParticipationRequestDto approveParticipationRequest(@PathVariable @PositiveOrZero final long userId,
                                                               @PathVariable @PositiveOrZero final long eventId,
                                                               @PathVariable @PositiveOrZero final long requestId) {
        return participationRequestService.approveParticipationRequest(userId, eventId, requestId);
    }

    @PatchMapping("/events/{eventId}/requests/{requestId}/reject")
    public ParticipationRequestDto declineParticipationRequest(@PathVariable @PositiveOrZero final long userId,
                                                               @PathVariable @PositiveOrZero final long eventId,
                                                               @PathVariable @PositiveOrZero final long requestId) {
        return participationRequestService.declineParticipationRequest(userId, eventId, requestId);
    }
}
