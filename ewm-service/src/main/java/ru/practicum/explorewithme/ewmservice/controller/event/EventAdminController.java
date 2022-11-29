package ru.practicum.explorewithme.ewmservice.controller.event;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.event.AdminUpdateEventRequest;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventState;
import ru.practicum.explorewithme.ewmservice.EwmClient;
import ru.practicum.explorewithme.ewmservice.service.event.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
@Validated
public class EventAdminController {
    private final EventService eventService;
    private final EwmClient ewmClient;

    @GetMapping
    public List<EventFullDto> getEvents(
        @RequestParam(required = false) final List<Long> users,
        @RequestParam(required = false) final List<EventState> states,
        @RequestParam(required = false) final List<Long> categories,
        @RequestParam(required = false) final LocalDateTime rangeStart,
        @RequestParam(required = false) final LocalDateTime rangeEnd,
        @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
        @RequestParam(defaultValue = "10") @Positive final int size
    ) {
        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto updateEventForAdmin(@PathVariable @PositiveOrZero final long eventId,
                                            @Valid @RequestBody final AdminUpdateEventRequest adminUpdate) {
        return eventService.updateEventForAdmin(eventId, adminUpdate);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable @PositiveOrZero final long eventId) {
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable @PositiveOrZero final long eventId) {
        return eventService.rejectEvent(eventId);
    }
}
