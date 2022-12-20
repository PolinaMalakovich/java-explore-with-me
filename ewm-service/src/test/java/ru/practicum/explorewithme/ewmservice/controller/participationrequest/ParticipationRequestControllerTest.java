package ru.practicum.explorewithme.ewmservice.controller.participationrequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestDto;
import ru.practicum.explorewithme.ewmservice.service.participationrequest.ParticipationRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestStatus.*;

@WebMvcTest(controllers = ParticipationRequestController.class)
class ParticipationRequestControllerTest {
    private final LocalDateTime created = LocalDateTime.of(2022, 12, 20, 15, 50, 0);
    private final String dateTime = "2022-12-20 15:50:00";
    private final ParticipationRequestDto pr = new ParticipationRequestDto(1L, 1L, 1L, created, PENDING);

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ParticipationRequestService service;

    @Autowired
    private MockMvc mvc;

    @Test
    void getParticipationRequests() throws Exception {
        when(service.getParticipationRequests(1L)).thenReturn(List.of(pr));
        mvc.perform(
                get("/users/{userId}/requests", 1L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id", is(pr.getId()), Long.class))
            .andExpect(jsonPath("$.[0].requester", is(pr.getRequester()), Long.class))
            .andExpect(jsonPath("$.[0].event", is(pr.getEvent()), Long.class))
            .andExpect(jsonPath("$.[0].created", is(dateTime)))
            .andExpect(jsonPath("$.[0].status", is(pr.getStatus().name())));
    }

    @Test
    void addParticipationRequest() throws Exception {
        when(service.addParticipationRequest(1L, 1L)).thenReturn(pr);
        mvc.perform(
                post("/users/{userId}/requests?eventId=1", 1L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(pr.getId()), Long.class))
            .andExpect(jsonPath("$.requester", is(pr.getRequester()), Long.class))
            .andExpect(jsonPath("$.event", is(pr.getEvent()), Long.class))
            .andExpect(jsonPath("$.created", is(dateTime)))
            .andExpect(jsonPath("$.status", is(pr.getStatus().name())));
    }

    @Test
    void cancelParticipationRequest() throws Exception {
        final ParticipationRequestDto canceledPr = new ParticipationRequestDto(1L, 1L, 1L, created, CANCELED);
        when(service.cancelParticipationRequest(1L, 1L)).thenReturn(canceledPr);
        mvc.perform(
                patch("/users/{userId}/requests/{requestId}/cancel", 1L, 1L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(canceledPr.getId()), Long.class))
            .andExpect(jsonPath("$.requester", is(canceledPr.getRequester()), Long.class))
            .andExpect(jsonPath("$.event", is(canceledPr.getEvent()), Long.class))
            .andExpect(jsonPath("$.created", is(dateTime)))
            .andExpect(jsonPath("$.status", is(canceledPr.getStatus().name())));
    }

    @Test
    void getParticipationRequestsForEvent() throws Exception {
        when(service.getParticipationRequestsForEvent(1L, 1L)).thenReturn(List.of(pr));
        mvc.perform(
                get("/users/{userId}/events/{eventId}/requests", 1L, 1L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id", is(pr.getId()), Long.class))
            .andExpect(jsonPath("$.[0].requester", is(pr.getRequester()), Long.class))
            .andExpect(jsonPath("$.[0].event", is(pr.getEvent()), Long.class))
            .andExpect(jsonPath("$.[0].created", is(dateTime)))
            .andExpect(jsonPath("$.[0].status", is(pr.getStatus().name())));
    }

    @Test
    void approveParticipationRequest() throws Exception {
        final ParticipationRequestDto confirmedPr = new ParticipationRequestDto(1L, 1L, 1L, created, CONFIRMED);
        when(service.approveParticipationRequest(1L, 1L, 1L)).thenReturn(confirmedPr);
        mvc.perform(
                patch("/users/{userId}/events/{eventId}/requests/{requestId}/confirm", 1L, 1L, 1L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(confirmedPr.getId()), Long.class))
            .andExpect(jsonPath("$.requester", is(confirmedPr.getRequester()), Long.class))
            .andExpect(jsonPath("$.event", is(confirmedPr.getEvent()), Long.class))
            .andExpect(jsonPath("$.created", is(dateTime)))
            .andExpect(jsonPath("$.status", is(confirmedPr.getStatus().name())));
    }

    @Test
    void declineParticipationRequest() throws Exception {
        final ParticipationRequestDto rejectedPr = new ParticipationRequestDto(1L, 1L, 1L, created, REJECTED);
        when(service.declineParticipationRequest(1L, 1L, 1L)).thenReturn(rejectedPr);
        mvc.perform(
                patch("/users/{userId}/events/{eventId}/requests/{requestId}/reject", 1L, 1L, 1L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(rejectedPr.getId()), Long.class))
            .andExpect(jsonPath("$.requester", is(rejectedPr.getRequester()), Long.class))
            .andExpect(jsonPath("$.event", is(rejectedPr.getEvent()), Long.class))
            .andExpect(jsonPath("$.created", is(dateTime)))
            .andExpect(jsonPath("$.status", is(rejectedPr.getStatus().name())));
    }
}