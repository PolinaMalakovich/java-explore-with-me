package ru.practicum.explorewithme.ewmservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.event.*;
import ru.practicum.explorewithme.dto.hit.HitDto;
import ru.practicum.explorewithme.ewmservice.EwmClient;
import ru.practicum.explorewithme.ewmservice.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
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
        @RequestParam(required = false, defaultValue = "false") final Boolean onlyAvailable,
        @RequestParam(required = false) final EventSort sort,
        @RequestParam(required = false, defaultValue = "0") final int from,
        @RequestParam(required = false, defaultValue = "10") final int size,
        final HttpServletRequest request
    ) {
        ewmClient.hit(new HitDto(null, "EWM", "/events", request.getRemoteAddr(), LocalDateTime.now()));

        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEvent(@PathVariable(name = "id") final long eventId, final HttpServletRequest request) {
        EventFullDto event = eventService.getEvent(eventId);
        ewmClient.hit(new HitDto(null, "EWM", "/events/" + eventId, request.getRemoteAddr(), LocalDateTime.now()));

        return event;
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEventsByUser(@PathVariable final long userId,
                                               @RequestParam(required = false, defaultValue = "0") final int from,
                                               @RequestParam(required = false, defaultValue = "10") final int size) {
        return eventService.getEventsByUser(userId, from, size);
    }

    @PatchMapping("/users/{userId}/events")
    public EventFullDto updateEvent(@PathVariable final long userId,
                                    @RequestBody final UpdateEventRequest updateEventRequest) {
        return eventService.updateEvent(userId, updateEventRequest);
    }

    @PostMapping("/users/{userId}/events")
    public EventFullDto addEvent(@PathVariable final long userId, @RequestBody final NewEventDto newEventDto) {
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEvent(@PathVariable final long userId, @PathVariable final long eventId) {
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable final long userId, @PathVariable final long eventId) {
        return eventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> getEvents(
        @RequestParam(required = false) final List<Long> users,
        @RequestParam(required = false) final List<EventState> states,
        @RequestParam(required = false) final List<Long> categories,
        @RequestParam(required = false) final LocalDateTime rangeStart,
        @RequestParam(required = false) final LocalDateTime rangeEnd,
        @RequestParam(required = false, defaultValue = "0") final int from,
        @RequestParam(required = false, defaultValue = "10") final int size
    ) {
        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/admin/events/{eventId}")
    public EventFullDto updateEventForAdmin(@PathVariable final long eventId,
                                            @RequestBody final AdminUpdateEventRequest adminUpdate) {
        return eventService.updateEventForAdmin(eventId, adminUpdate);
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable final long eventId) {
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable final long eventId) {
        return eventService.rejectEvent(eventId);
    }
}
