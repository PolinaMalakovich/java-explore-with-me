package ru.practicum.explorewithme.ewmservice.controller.event;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.event.*;
import ru.practicum.explorewithme.ewmservice.EwmClient;
import ru.practicum.explorewithme.ewmservice.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventService eventService;
    private final EwmClient ewmClient;

    @GetMapping("/events")
    public List<EventShortDto> getEvents(
        @RequestParam(required = false) final String text,
        @RequestParam(required = false) final List<Long> categories,
        @RequestParam(required = false) final Boolean paid,
        @RequestParam(required = false) final LocalDateTime rangeStart,
        @RequestParam(required = false) final LocalDateTime rangeEnd,
        @RequestParam(defaultValue = "false") final Boolean onlyAvailable,
        @RequestParam(required = false) final EventSort sort,
        @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
        @RequestParam(defaultValue = "10") @PositiveOrZero final int size,
        final HttpServletRequest request
    ) {
        ewmClient.hit("/events", request.getRemoteAddr());

        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEvent(@PathVariable(name = "id") @PositiveOrZero final long eventId,
                                 final HttpServletRequest request) {
        EventFullDto event = eventService.getEvent(eventId);
        ewmClient.hit("/events/" + eventId, request.getRemoteAddr());

        return event;
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEventsByUser(@PathVariable @PositiveOrZero final long userId,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                               @RequestParam(defaultValue = "10") @PositiveOrZero final int size) {
        return eventService.getEventsByUser(userId, from, size);
    }

    @PatchMapping("/users/{userId}/events")
    public EventFullDto updateEvent(@PathVariable @PositiveOrZero final long userId,
                                    @Valid @RequestBody final UpdateEventRequest updateEventRequest) {
        return eventService.updateEvent(userId, updateEventRequest);
    }

    @PostMapping("/users/{userId}/events")
    public EventFullDto addEvent(@PathVariable @PositiveOrZero final long userId,
                                 @Valid @RequestBody final NewEventDto newEventDto) {
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEvent(@PathVariable @PositiveOrZero final long userId,
                                 @PathVariable @PositiveOrZero final long eventId) {
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable @PositiveOrZero final long userId,
                                    @PathVariable @PositiveOrZero final long eventId) {
        return eventService.cancelEvent(userId, eventId);
    }
}
