package ru.practicum.explorewithme.ewmservice.service.event;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.EventState;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.ewmservice.model.Category;
import ru.practicum.explorewithme.ewmservice.model.Event;
import ru.practicum.explorewithme.ewmservice.model.Location;
import ru.practicum.explorewithme.ewmservice.model.User;

import java.time.LocalDateTime;

import static ru.practicum.explorewithme.dto.event.EventState.PENDING;
import static ru.practicum.explorewithme.ewmservice.service.category.CategoryMapper.toCategory;
import static ru.practicum.explorewithme.ewmservice.service.category.CategoryMapper.toCategoryDto;
import static ru.practicum.explorewithme.ewmservice.service.event.LocationMapper.toLocation;
import static ru.practicum.explorewithme.ewmservice.service.event.LocationMapper.toLocationDto;
import static ru.practicum.explorewithme.ewmservice.service.user.UserMapper.toUser;
import static ru.practicum.explorewithme.ewmservice.service.user.UserMapper.toUserShortDto;

@UtilityClass
public class EventMapper {
    public static EventFullDto toEventFullDto(final Event event, final Long confirmedRequests, final Long views) {
        return new EventFullDto(
            event.getId(),
            event.getTitle(),
            event.getAnnotation(),
            toCategoryDto(event.getCategory()),
            event.getDescription(),
            event.getCreatedOn(),
            event.getPublishedOn(),
            event.getEventDate(),
            toLocationDto(event.getLocation()),
            event.isPaid(),
            toUserShortDto(event.getInitiator()),
            event.getParticipantLimit(),
            event.isRequestModeration(),
            event.getState(),
            confirmedRequests,
            views
        );
    }

    public static Event toEvent(final EventFullDto eventFullDto,
                                final String email) {
        return new Event(
            eventFullDto.getId(),
            eventFullDto.getTitle(),
            eventFullDto.getAnnotation(),
            toCategory(eventFullDto.getCategory()),
            eventFullDto.getDescription(),
            eventFullDto.getCreatedOn(),
            eventFullDto.getPublishedOn(),
            eventFullDto.getEventDate(),
            toLocation(eventFullDto.getLocation()),
            eventFullDto.isPaid(),
            toUser(eventFullDto.getInitiator(), email),
            eventFullDto.getParticipantLimit(),
            eventFullDto.getRequestModeration(),
            eventFullDto.getState()
        );
    }

    public static EventShortDto toEventShortDto(final Event event, final Long confirmedRequests, final Long views) {
        return new EventShortDto(
            event.getId(),
            event.getTitle(),
            event.getAnnotation(),
            toCategoryDto(event.getCategory()),
            event.getEventDate(),
            toUserShortDto(event.getInitiator()),
            event.isPaid(),
            confirmedRequests,
            views
        );
    }

    public static Event toEvent(final EventShortDto eventShortDto,
                                final String description,
                                final LocalDateTime createdOn,
                                final LocalDateTime publishedOn,
                                final Location location,
                                final String email,
                                final int participantLimit,
                                final boolean requestModeration,
                                final EventState eventState) {
        return new Event(
            eventShortDto.getId(),
            eventShortDto.getTitle(),
            eventShortDto.getAnnotation(),
            toCategory(eventShortDto.getCategory()),
            description,
            createdOn,
            publishedOn,
            eventShortDto.getEventDate(),
            location,
            eventShortDto.isPaid(),
            toUser(eventShortDto.getInitiator(), email),
            participantLimit,
            requestModeration,
            eventState
        );
    }

    public static Event toEvent(final User user, final NewEventDto newEventDto, final Category category) {
        return new Event(
            null,
            newEventDto.getTitle(),
            newEventDto.getAnnotation(),
            category,
            newEventDto.getDescription(),
            LocalDateTime.now(),
            null,
            newEventDto.getEventDate(),
            toLocation(newEventDto.getLocation()),
            newEventDto.isPaid(),
            user,
            newEventDto.getParticipantLimit(),
            newEventDto.isRequestModeration(),
            PENDING
        );
    }
}
