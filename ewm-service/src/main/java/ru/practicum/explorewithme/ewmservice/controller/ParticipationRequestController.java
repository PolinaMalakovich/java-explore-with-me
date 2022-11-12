package ru.practicum.explorewithme.ewmservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestDto;
import ru.practicum.explorewithme.ewmservice.service.participationrequest.ParticipationRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}")
@RequiredArgsConstructor
public class ParticipationRequestController {
    private final ParticipationRequestService participationRequestService;

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getParticipationRequests(@PathVariable final long userId) {
        return participationRequestService.getParticipationRequests(userId);
    }

    @PostMapping("/requests")
    public ParticipationRequestDto addParticipationRequest(@PathVariable final long userId,
                                                           @RequestParam final long eventId) {
        return participationRequestService.addParticipationRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable final long userId,
                                                              @PathVariable final long requestId) {
        return participationRequestService.cancelParticipationRequest(userId, requestId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequestsForEvent(@PathVariable final long userId,
                                                                          @PathVariable final long eventId) {
        return participationRequestService.getParticipationRequestsForEvent(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests/{requestId}/confirm")
    public ParticipationRequestDto approveParticipationRequest(@PathVariable final long userId,
                                                               @PathVariable final long eventId,
                                                               @PathVariable final long requestId) {
        return participationRequestService.approveParticipationRequest(userId, eventId, requestId);
    }

    @PatchMapping("/events/{eventId}/requests/{requestId}/reject")
    public ParticipationRequestDto declineParticipationRequest(@PathVariable final long userId,
                                                               @PathVariable final long eventId,
                                                               @PathVariable final long requestId) {
        return participationRequestService.declineParticipationRequest(userId, eventId, requestId);
    }
}
