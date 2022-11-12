package ru.practicum.explorewithme.ewmservice.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.event.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort, int from,
                                         int size) {
        return null;
    }

    @Override
    public EventFullDto getEvent(long eventId) {
        return null;
    }

    @Override
    public List<EventShortDto> getEventsByUser(long userId, int from, int size) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(long userId, UpdateEventRequest event) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto addEvent(long userId, NewEventDto event) {
        return null;
    }

    @Override
    public EventFullDto getEvent(long userId, long eventId) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto cancelEvent(long userId, long eventId) {
        return null;
    }

    @Override
    public List<EventFullDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto updateEventForAdmin(long eventId, AdminUpdateEventRequest event) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto publishEvent(long eventId) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto rejectEvent(long eventId) {
        return null;
    }
}
