package ru.practicum.explorewithme.ewmservice.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.ewmservice.service.comment.CommentService;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentAdminController.class)
class CommentAdminControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    CommentService service;

    @Autowired
    private MockMvc mvc;

    @Test
    void deleteComment() throws Exception {
        mvc.perform(
                delete("/admin/events/{eventId}/comments/{commentId}", 1L, 1L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());

        verify(service, times(1)).deleteCommentForAdmin(anyLong(), anyLong());
    }
}