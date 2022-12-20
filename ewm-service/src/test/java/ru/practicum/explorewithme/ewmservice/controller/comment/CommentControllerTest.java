package ru.practicum.explorewithme.ewmservice.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.UpdateCommentRequest;
import ru.practicum.explorewithme.ewmservice.service.comment.CommentService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {
    private final LocalDateTime created = LocalDateTime.of(2022, 12, 20, 17, 30, 0);
    private final String dateTime = "2022-12-20 17:30:00";
    private final CommentDto comment = new CommentDto(1L, "Nice!", 1L, 1L, "Alice", created);
    private final UpdateCommentRequest update = new UpdateCommentRequest(1L, "Nice!", 1L);

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CommentService service;

    @Autowired
    private MockMvc mvc;

    @Test
    void addComment() throws Exception {
        when(service.addComment(anyLong(), any())).thenReturn(comment);
        mvc.perform(
                post("/events/{eventId}/comment", 1L)
                    .content(mapper.writeValueAsString(comment))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
            .andExpect(jsonPath("$.text", is(comment.getText())))
            .andExpect(jsonPath("$.eventId", is(comment.getEventId()), Long.class))
            .andExpect(jsonPath("$.authorId", is(comment.getAuthorId()), Long.class))
            .andExpect(jsonPath("$.authorName", is(comment.getAuthorName())))
            .andExpect(jsonPath("$.created", is(dateTime)));
    }

    @Test
    void getComment() throws Exception {
        when(service.getComment(anyLong(), anyLong())).thenReturn(comment);
        mvc.perform(
                get("/events/{eventId}/comments/{commentId}", 1L, 1L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
            .andExpect(jsonPath("$.text", is(comment.getText())))
            .andExpect(jsonPath("$.eventId", is(comment.getEventId()), Long.class))
            .andExpect(jsonPath("$.authorId", is(comment.getAuthorId()), Long.class))
            .andExpect(jsonPath("$.authorName", is(comment.getAuthorName())))
            .andExpect(jsonPath("$.created", is(dateTime)));
    }

    @Test
    void getComments() throws Exception {
        when(service.getComments(anyLong(), anyInt(), anyInt())).thenReturn(List.of(comment));
        mvc.perform(
                get("/events/{eventId}/comments", 1L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id", is(comment.getId()), Long.class))
            .andExpect(jsonPath("$.[0].text", is(comment.getText())))
            .andExpect(jsonPath("$.[0].eventId", is(comment.getEventId()), Long.class))
            .andExpect(jsonPath("$.[0].authorId", is(comment.getAuthorId()), Long.class))
            .andExpect(jsonPath("$.[0].authorName", is(comment.getAuthorName())))
            .andExpect(jsonPath("$.[0].created", is(dateTime)));
    }

    @Test
    void updateComment() throws Exception {
        when(service.updateComment(anyLong(), any())).thenReturn(comment);
        mvc.perform(
                patch("/events/{eventId}/comments", 1L)
                    .content(mapper.writeValueAsString(update))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
            .andExpect(jsonPath("$.text", is(comment.getText())))
            .andExpect(jsonPath("$.eventId", is(comment.getEventId()), Long.class))
            .andExpect(jsonPath("$.authorId", is(comment.getAuthorId()), Long.class))
            .andExpect(jsonPath("$.authorName", is(comment.getAuthorName())))
            .andExpect(jsonPath("$.created", is(dateTime)));
    }

    @Test
    void deleteComment() throws Exception {
        mvc.perform(
                delete("/events/{eventId}/comments/{commentId}?userId=1", 1L, 1L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());

        verify(service, times(1)).deleteComment(anyLong(), anyLong(), anyLong());
    }
}