package ru.practicum.explorewithme.ewmservice.controller.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.event.AdminUpdateEventRequest;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.LocationDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;
import ru.practicum.explorewithme.ewmservice.EwmClient;
import ru.practicum.explorewithme.ewmservice.service.event.EventService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.explorewithme.dto.event.EventState.CANCELED;
import static ru.practicum.explorewithme.dto.event.EventState.PUBLISHED;

@WebMvcTest(controllers = EventAdminController.class)
class EventAdminControllerTest {
    private final CategoryDto category = new CategoryDto(1L, "Category");
    private final LocationDto location = new LocationDto(0.0, 0.0);
    private final UserShortDto user = new UserShortDto(1L, "Username");
    private final EventFullDto event = new EventFullDto(
        1L,
        "Event",
        "Event annotation",
        category,
        "Event description",
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
        when(service.getEvents(null, null, null, null, null, 0, 10))
            .thenReturn(List.of(event));
        mvc.perform(
                get("/admin/events")
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id", is(event.getId()), Long.class))
            .andExpect(jsonPath("$.[0].title", is(event.getTitle())))
            .andExpect(jsonPath("$.[0].annotation", is(event.getAnnotation())))
            .andExpect(jsonPath("$.[0].category.id", is(event.getCategory().getId()), Long.class))
            .andExpect(jsonPath("$.[0].category.name", is(event.getCategory().getName())))
            .andExpect(jsonPath("$.[0].createdOn", is(createdOn)))
            .andExpect(jsonPath("$.[0].publishedOn", is(publishedOn)))
            .andExpect(jsonPath("$.[0].eventDate", is(eventDate)))
            .andExpect(jsonPath("$.[0].location.lat", is(event.getLocation().getLat()), Double.class))
            .andExpect(jsonPath("$.[0].location.lon", is(event.getLocation().getLon()), Double.class))
            .andExpect(jsonPath("$.[0].paid", is(event.isPaid())))
            .andExpect(jsonPath("$.[0].initiator.id", is(event.getInitiator().getId()), Long.class))
            .andExpect(jsonPath("$.[0].initiator.name", is(event.getInitiator().getName())))
            .andExpect(jsonPath("$.[0].participantLimit", is(event.getParticipantLimit()), Integer.class))
            .andExpect(jsonPath("$.[0].requestModeration", is(event.getRequestModeration())))
            .andExpect(jsonPath("$.[0].state", is(event.getState().name())))
            .andExpect(jsonPath("$.[0].confirmedRequests", is(event.getConfirmedRequests()), Long.class))
            .andExpect(jsonPath("$.[0].views", is(event.getViews()), Long.class));
        verify(service, times(1)).getEvents(null, null, null, null, null, 0, 10);
    }

    @Test
    void updateEventForAdmin() throws Exception {
        final AdminUpdateEventRequest adminUpdate = new AdminUpdateEventRequest(
            null, null, null, null, null, null, true, 100, null
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
        when(service.updateEventForAdmin(anyLong(), any())).thenReturn(updatedEvent);
        mvc.perform(
                put("/admin/events/{eventId}", event.getId())
                    .content(mapper.writeValueAsString(adminUpdate))
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
    }

    @Test
    void publishEvent() throws Exception {
        when(service.publishEvent(anyLong())).thenReturn(event);
        mvc.perform(
                patch("/admin/events/{eventId}/publish", event.getId())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(event.getId()), Long.class))
            .andExpect(jsonPath("$.title", is(event.getTitle())))
            .andExpect(jsonPath("$.annotation", is(event.getAnnotation())))
            .andExpect(jsonPath("$.category.id", is(event.getCategory().getId()), Long.class))
            .andExpect(jsonPath("$.category.name", is(event.getCategory().getName())))
            .andExpect(jsonPath("$.createdOn", is(createdOn)))
            .andExpect(jsonPath("$.publishedOn", is(publishedOn)))
            .andExpect(jsonPath("$.eventDate", is(eventDate)))
            .andExpect(jsonPath("$.location.lat", is(event.getLocation().getLat()), Double.class))
            .andExpect(jsonPath("$.location.lon", is(event.getLocation().getLon()), Double.class))
            .andExpect(jsonPath("$.paid", is(event.isPaid())))
            .andExpect(jsonPath("$.initiator.id", is(event.getInitiator().getId()), Long.class))
            .andExpect(jsonPath("$.initiator.name", is(event.getInitiator().getName())))
            .andExpect(jsonPath("$.participantLimit", is(event.getParticipantLimit()), Integer.class))
            .andExpect(jsonPath("$.requestModeration", is(event.getRequestModeration())))
            .andExpect(jsonPath("$.state", is(event.getState().name())))
            .andExpect(jsonPath("$.confirmedRequests", is(event.getConfirmedRequests()), Long.class))
            .andExpect(jsonPath("$.views", is(event.getViews()), Long.class));
    }

    @Test
    void rejectEvent() throws Exception {
        final EventFullDto event = new EventFullDto(
            1L,
            "Event",
            "Event annotation",
            category,
            "Event description",
            LocalDateTime.of(2022, 12, 21, 17, 50, 0),
            null,
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
        when(service.rejectEvent(anyLong())).thenReturn(event);
        mvc.perform(
                patch("/admin/events/{eventId}/reject", event.getId())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(event.getId()), Long.class))
            .andExpect(jsonPath("$.title", is(event.getTitle())))
            .andExpect(jsonPath("$.annotation", is(event.getAnnotation())))
            .andExpect(jsonPath("$.category.id", is(event.getCategory().getId()), Long.class))
            .andExpect(jsonPath("$.category.name", is(event.getCategory().getName())))
            .andExpect(jsonPath("$.createdOn", is(createdOn)))
            .andExpect(jsonPath("$.publishedOn", is(event.getPublishedOn())))
            .andExpect(jsonPath("$.eventDate", is(eventDate)))
            .andExpect(jsonPath("$.location.lat", is(event.getLocation().getLat()), Double.class))
            .andExpect(jsonPath("$.location.lon", is(event.getLocation().getLon()), Double.class))
            .andExpect(jsonPath("$.paid", is(event.isPaid())))
            .andExpect(jsonPath("$.initiator.id", is(event.getInitiator().getId()), Long.class))
            .andExpect(jsonPath("$.initiator.name", is(event.getInitiator().getName())))
            .andExpect(jsonPath("$.participantLimit", is(event.getParticipantLimit()), Integer.class))
            .andExpect(jsonPath("$.requestModeration", is(event.getRequestModeration())))
            .andExpect(jsonPath("$.state", is(event.getState().name())))
            .andExpect(jsonPath("$.confirmedRequests", is(event.getConfirmedRequests()), Long.class))
            .andExpect(jsonPath("$.views", is(event.getViews()), Long.class));
    }
}