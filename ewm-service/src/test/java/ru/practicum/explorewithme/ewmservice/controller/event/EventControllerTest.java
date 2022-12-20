package ru.practicum.explorewithme.ewmservice.controller.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.event.*;
import ru.practicum.explorewithme.dto.user.UserShortDto;
import ru.practicum.explorewithme.ewmservice.EwmClient;
import ru.practicum.explorewithme.ewmservice.service.event.EventService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.explorewithme.dto.event.EventState.CANCELED;
import static ru.practicum.explorewithme.dto.event.EventState.PUBLISHED;

@WebMvcTest(controllers = EventController.class)
class EventControllerTest {
    private final CategoryDto category = new CategoryDto(1L, "Category");
    private final LocationDto location = new LocationDto(0.0, 0.0);
    private final UserShortDto user = new UserShortDto(1L, "Username");
    private final EventFullDto eventFull = new EventFullDto(
        1L,
        "Event",
        "This is an event annotation for you learn what the event is all about.",
        category,
        "This is an event description which provides more detail than the annotation for you to have a better understanding of the event.",
        LocalDateTime.of(2022, 12, 21, 17, 50, 0),
        LocalDateTime.of(2022, 12, 21, 18, 50, 0),
        LocalDateTime.of(2023, 1, 21, 17, 0, 0),
        location,
        false,
        user,
        0,
        false,
        PUBLISHED,
        0L,
        0L
    );
    private final EventShortDto eventShort = new EventShortDto(
        1L,
        "Event",
        "This is an event annotation for you learn what the event is all about.",
        category,
        LocalDateTime.of(2023, 1, 21, 17, 0, 0),
        user,
        false,
        0L,
        0L
    );
    private final String createdOn = "2022-12-21 17:50:00";
    private final String publishedOn = "2022-12-21 18:50:00";
    private final String eventDate = "2023-01-21 17:00:00";

    @Autowired
    ObjectMapper mapper;

    @MockBean
    EventService service;

    @MockBean
    EwmClient client;

    @Autowired
    private MockMvc mvc;

    @Test
    void getEvents() throws Exception {
        when(service.getEvents(null, null, null, null, null, false, null, 0, 10))
            .thenReturn(List.of(eventShort));
        mvc.perform(
                get("/events")
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id", is(eventShort.getId()), Long.class))
            .andExpect(jsonPath("$.[0].title", is(eventShort.getTitle())))
            .andExpect(jsonPath("$.[0].annotation", is(eventShort.getAnnotation())))
            .andExpect(jsonPath("$.[0].category.id", is(eventShort.getCategory().getId()), Long.class))
            .andExpect(jsonPath("$.[0].category.name", is(eventShort.getCategory().getName())))
            .andExpect(jsonPath("$.[0].eventDate", is(eventDate)))
            .andExpect(jsonPath("$.[0].initiator.id", is(eventShort.getInitiator().getId()), Long.class))
            .andExpect(jsonPath("$.[0].initiator.name", is(eventShort.getInitiator().getName())))
            .andExpect(jsonPath("$.[0].paid", is(eventShort.isPaid())))
            .andExpect(jsonPath("$.[0].confirmedRequests", is(eventShort.getConfirmedRequests()), Long.class))
            .andExpect(jsonPath("$.[0].views", is(eventShort.getViews()), Long.class));
        verify(service, times(1)).getEvents(null, null, null, null, null, false, null, 0, 10);
    }

    @Test
    void getEvent() throws Exception {
        when(service.getEvent(eventFull.getId())).thenReturn(eventFull);
        mvc.perform(
                get("/events/{id}", eventFull.getId())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(eventFull.getId()), Long.class))
            .andExpect(jsonPath("$.title", is(eventFull.getTitle())))
            .andExpect(jsonPath("$.annotation", is(eventFull.getAnnotation())))
            .andExpect(jsonPath("$.category.id", is(eventFull.getCategory().getId()), Long.class))
            .andExpect(jsonPath("$.category.name", is(eventFull.getCategory().getName())))
            .andExpect(jsonPath("$.createdOn", is(createdOn)))
            .andExpect(jsonPath("$.publishedOn", is(publishedOn)))
            .andExpect(jsonPath("$.eventDate", is(eventDate)))
            .andExpect(jsonPath("$.location.lat", is(eventFull.getLocation().getLat()), Double.class))
            .andExpect(jsonPath("$.location.lon", is(eventFull.getLocation().getLon()), Double.class))
            .andExpect(jsonPath("$.paid", is(eventFull.isPaid())))
            .andExpect(jsonPath("$.initiator.id", is(eventFull.getInitiator().getId()), Long.class))
            .andExpect(jsonPath("$.initiator.name", is(eventFull.getInitiator().getName())))
            .andExpect(jsonPath("$.participantLimit", is(eventFull.getParticipantLimit()), Integer.class))
            .andExpect(jsonPath("$.requestModeration", is(eventFull.getRequestModeration())))
            .andExpect(jsonPath("$.state", is(eventFull.getState().name())))
            .andExpect(jsonPath("$.confirmedRequests", is(eventFull.getConfirmedRequests()), Long.class))
            .andExpect(jsonPath("$.views", is(eventFull.getViews()), Long.class));
        verify(service, times(1)).getEvent(eventFull.getId());
    }

    @Test
    void getEventsByUser() throws Exception {
        when(service.getEventsByUser(user.getId(), 0, 10)).thenReturn(List.of(eventShort));
        mvc.perform(
                get("/users/{userId}/events", user.getId())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id", is(eventShort.getId()), Long.class))
            .andExpect(jsonPath("$.[0].title", is(eventShort.getTitle())))
            .andExpect(jsonPath("$.[0].annotation", is(eventShort.getAnnotation())))
            .andExpect(jsonPath("$.[0].category.id", is(eventShort.getCategory().getId()), Long.class))
            .andExpect(jsonPath("$.[0].category.name", is(eventShort.getCategory().getName())))
            .andExpect(jsonPath("$.[0].eventDate", is(eventDate)))
            .andExpect(jsonPath("$.[0].initiator.id", is(eventShort.getInitiator().getId()), Long.class))
            .andExpect(jsonPath("$.[0].initiator.name", is(eventShort.getInitiator().getName())))
            .andExpect(jsonPath("$.[0].paid", is(eventShort.isPaid())))
            .andExpect(jsonPath("$.[0].confirmedRequests", is(eventShort.getConfirmedRequests()), Long.class))
            .andExpect(jsonPath("$.[0].views", is(eventShort.getViews()), Long.class));
        verify(service, times(1)).getEventsByUser(user.getId(), 0, 10);
    }

    @Test
    void updateEvent() throws Exception {
        final UpdateEventRequest updateRequest = new UpdateEventRequest(
            eventFull.getId(), null, null, null, null, null, true, 100
        );
        final EventFullDto updatedEvent = new EventFullDto(
            1L,
            "Event",
            "Event annotation",
            category,
            "Event description",
            LocalDateTime.of(2022, 12, 21, 17, 50, 0),
            LocalDateTime.of(2022, 12, 21, 18, 50, 0),
            LocalDateTime.of(2023, 1, 21, 17, 0, 0),
            location,
            true,
            user,
            100,
            false,
            PUBLISHED,
            0L,
            0L
        );
        when(service.updateEvent(user.getId(), updateRequest)).thenReturn(updatedEvent);
        mvc.perform(
                patch("/users/{userId}/events", user.getId())
                    .content(mapper.writeValueAsString(updateRequest))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(updatedEvent.getId()), Long.class))
            .andExpect(jsonPath("$.title", is(updatedEvent.getTitle())))
            .andExpect(jsonPath("$.annotation", is(updatedEvent.getAnnotation())))
            .andExpect(jsonPath("$.category.id", is(updatedEvent.getCategory().getId()), Long.class))
            .andExpect(jsonPath("$.category.name", is(updatedEvent.getCategory().getName())))
            .andExpect(jsonPath("$.createdOn", is(createdOn)))
            .andExpect(jsonPath("$.publishedOn", is(publishedOn)))
            .andExpect(jsonPath("$.eventDate", is(eventDate)))
            .andExpect(jsonPath("$.location.lat", is(updatedEvent.getLocation().getLat()), Double.class))
            .andExpect(jsonPath("$.location.lon", is(updatedEvent.getLocation().getLon()), Double.class))
            .andExpect(jsonPath("$.paid", is(updatedEvent.isPaid())))
            .andExpect(jsonPath("$.initiator.id", is(updatedEvent.getInitiator().getId()), Long.class))
            .andExpect(jsonPath("$.initiator.name", is(updatedEvent.getInitiator().getName())))
            .andExpect(jsonPath("$.participantLimit", is(updatedEvent.getParticipantLimit()), Integer.class))
            .andExpect(jsonPath("$.requestModeration", is(updatedEvent.getRequestModeration())))
            .andExpect(jsonPath("$.state", is(updatedEvent.getState().name())))
            .andExpect(jsonPath("$.confirmedRequests", is(updatedEvent.getConfirmedRequests()), Long.class))
            .andExpect(jsonPath("$.views", is(updatedEvent.getViews()), Long.class));
        verify(service, times(1)).updateEvent(user.getId(), updateRequest);
    }

    @Test
    void addEvent() throws Exception {
        final NewEventDto newEvent = new NewEventDto(
            "Event",
            "This is an event annotation for you learn what the event is all about.",
            category.getId(),
            "This is an event description which provides more detail than the annotation for you to have a better understanding of the event.",
            LocalDateTime.of(2023, 1, 21, 17, 0, 0),
            location,
            false,
            0,
            false
        );
        when(service.addEvent(user.getId(), newEvent)).thenReturn(eventFull);
        mvc.perform(
                post("/users/{userId}/events", user.getId())
                    .content(mapper.writeValueAsString(newEvent))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(eventFull.getId()), Long.class))
            .andExpect(jsonPath("$.title", is(eventFull.getTitle())))
            .andExpect(jsonPath("$.annotation", is(eventFull.getAnnotation())))
            .andExpect(jsonPath("$.category.id", is(eventFull.getCategory().getId()), Long.class))
            .andExpect(jsonPath("$.category.name", is(eventFull.getCategory().getName())))
            .andExpect(jsonPath("$.createdOn", is(createdOn)))
            .andExpect(jsonPath("$.publishedOn", is(publishedOn)))
            .andExpect(jsonPath("$.eventDate", is(eventDate)))
            .andExpect(jsonPath("$.location.lat", is(eventFull.getLocation().getLat()), Double.class))
            .andExpect(jsonPath("$.location.lon", is(eventFull.getLocation().getLon()), Double.class))
            .andExpect(jsonPath("$.paid", is(eventFull.isPaid())))
            .andExpect(jsonPath("$.initiator.id", is(eventFull.getInitiator().getId()), Long.class))
            .andExpect(jsonPath("$.initiator.name", is(eventFull.getInitiator().getName())))
            .andExpect(jsonPath("$.participantLimit", is(eventFull.getParticipantLimit()), Integer.class))
            .andExpect(jsonPath("$.requestModeration", is(eventFull.getRequestModeration())))
            .andExpect(jsonPath("$.state", is(eventFull.getState().name())))
            .andExpect(jsonPath("$.confirmedRequests", is(eventFull.getConfirmedRequests()), Long.class))
            .andExpect(jsonPath("$.views", is(eventFull.getViews()), Long.class));
        verify(service, times(1)).addEvent(user.getId(), newEvent);
    }

    @Test
    void getEventByUser() throws Exception {
        when(service.getEvent(user.getId(), eventFull.getId())).thenReturn(eventFull);
        mvc.perform(
                get("/users/{userId}/events/{eventId}", user.getId(), eventFull.getId())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(eventFull.getId()), Long.class))
            .andExpect(jsonPath("$.title", is(eventFull.getTitle())))
            .andExpect(jsonPath("$.annotation", is(eventFull.getAnnotation())))
            .andExpect(jsonPath("$.category.id", is(eventFull.getCategory().getId()), Long.class))
            .andExpect(jsonPath("$.category.name", is(eventFull.getCategory().getName())))
            .andExpect(jsonPath("$.createdOn", is(createdOn)))
            .andExpect(jsonPath("$.publishedOn", is(publishedOn)))
            .andExpect(jsonPath("$.eventDate", is(eventDate)))
            .andExpect(jsonPath("$.location.lat", is(eventFull.getLocation().getLat()), Double.class))
            .andExpect(jsonPath("$.location.lon", is(eventFull.getLocation().getLon()), Double.class))
            .andExpect(jsonPath("$.paid", is(eventFull.isPaid())))
            .andExpect(jsonPath("$.initiator.id", is(eventFull.getInitiator().getId()), Long.class))
            .andExpect(jsonPath("$.initiator.name", is(eventFull.getInitiator().getName())))
            .andExpect(jsonPath("$.participantLimit", is(eventFull.getParticipantLimit()), Integer.class))
            .andExpect(jsonPath("$.requestModeration", is(eventFull.getRequestModeration())))
            .andExpect(jsonPath("$.state", is(eventFull.getState().name())))
            .andExpect(jsonPath("$.confirmedRequests", is(eventFull.getConfirmedRequests()), Long.class))
            .andExpect(jsonPath("$.views", is(eventFull.getViews()), Long.class));
        verify(service, times(1)).getEvent(user.getId(), eventFull.getId());
    }

    @Test
    void cancelEvent() throws Exception {
        final EventFullDto canceledEvent = new EventFullDto(
            1L,
            "Event",
            "This is an event annotation for you learn what the event is all about.",
            category,
            "This is an event description which provides more detail than the annotation for you to have a better understanding of the event.",
            LocalDateTime.of(2022, 12, 21, 17, 50, 0),
            LocalDateTime.of(2022, 12, 21, 18, 50, 0),
            LocalDateTime.of(2023, 1, 21, 17, 0, 0),
            location,
            false,
            user,
            0,
            false,
            CANCELED,
            0L,
            0L
        );
        when(service.cancelEvent(user.getId(), eventFull.getId())).thenReturn(canceledEvent);
        mvc.perform(
                patch("/users/{userId}/events/{eventId}", user.getId(), eventFull.getId())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(canceledEvent.getId()), Long.class))
            .andExpect(jsonPath("$.title", is(canceledEvent.getTitle())))
            .andExpect(jsonPath("$.annotation", is(canceledEvent.getAnnotation())))
            .andExpect(jsonPath("$.category.id", is(canceledEvent.getCategory().getId()), Long.class))
            .andExpect(jsonPath("$.category.name", is(canceledEvent.getCategory().getName())))
            .andExpect(jsonPath("$.createdOn", is(createdOn)))
            .andExpect(jsonPath("$.publishedOn", is(publishedOn)))
            .andExpect(jsonPath("$.eventDate", is(eventDate)))
            .andExpect(jsonPath("$.location.lat", is(canceledEvent.getLocation().getLat()), Double.class))
            .andExpect(jsonPath("$.location.lon", is(canceledEvent.getLocation().getLon()), Double.class))
            .andExpect(jsonPath("$.paid", is(canceledEvent.isPaid())))
            .andExpect(jsonPath("$.initiator.id", is(canceledEvent.getInitiator().getId()), Long.class))
            .andExpect(jsonPath("$.initiator.name", is(canceledEvent.getInitiator().getName())))
            .andExpect(jsonPath("$.participantLimit", is(canceledEvent.getParticipantLimit()), Integer.class))
            .andExpect(jsonPath("$.requestModeration", is(canceledEvent.getRequestModeration())))
            .andExpect(jsonPath("$.state", is(canceledEvent.getState().name())))
            .andExpect(jsonPath("$.confirmedRequests", is(canceledEvent.getConfirmedRequests()), Long.class))
            .andExpect(jsonPath("$.views", is(canceledEvent.getViews()), Long.class));
        verify(service, times(1)).cancelEvent(user.getId(), eventFull.getId());
    }
}