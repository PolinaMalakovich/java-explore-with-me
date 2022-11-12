package ru.practicum.explorewithme.ewmservice.service.event;

import ru.practicum.explorewithme.dto.event.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventShortDto> getEvents(String text,
                                  List<Long> categories,
                                  Boolean paid,
                                  LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd,
                                  Boolean onlyAvailable,
                                  EventSort sort,
                                  int from,
                                  int size);

    EventFullDto getEvent(long eventId);

    List<EventShortDto> getEventsByUser(long userId, int from, int size);

    EventFullDto updateEvent(long userId, UpdateEventRequest event);

    EventFullDto addEvent(long userId, NewEventDto event);

    EventFullDto getEvent(long userId, long eventId);

    EventFullDto cancelEvent(long userId, long eventId);

    List<EventFullDto> getEvents(List<Long> users,
                                 List<EventState> states,
                                 List<Long> categories,
                                 LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd,
                                 int from,
                                 int size);

    EventFullDto updateEventForAdmin(long eventId, AdminUpdateEventRequest event);

    EventFullDto publishEvent(long eventId);

    EventFullDto rejectEvent(long eventId);
}
