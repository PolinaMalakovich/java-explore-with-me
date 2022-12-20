package ru.practicum.explorewithme.ewmservice.controller.compilation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;
import ru.practicum.explorewithme.ewmservice.service.compilation.CompilationService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CompilationController.class)
class CompilationControllerTest {
    private final UserShortDto mark = new UserShortDto(1L, "Mark");
    private final UserShortDto gerard = new UserShortDto(2L, "Gerard");
    private final CategoryDto category = new CategoryDto(1L, "Concerts");
    private final EventShortDto blink128 = new EventShortDto(
        1L,
        "Blink 128",
        "Blink 128 reunion concert",
        category,
        LocalDateTime.of(2022, 12, 21, 21, 0, 0),
        mark,
        true,
        0L,
        0L
    );
    private final String blinkTime = "2022-12-21 21:00:00";
    private final EventShortDto mcr = new EventShortDto(
        2L,
        "My Chemical Romance",
        "My Chemical Romance reunion concert",
        category,
        LocalDateTime.of(2023, 1, 21, 21, 0, 0),
        gerard,
        true,
        0L,
        0L
    );
    private final String mcrTime = "2023-01-21 21:00:00";
    private final CompilationDto compilation = new CompilationDto(
        1L,
        "Reunion Concerts",
        false,
        List.of(blink128, mcr)
    );

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CompilationService service;

    @Autowired
    private MockMvc mvc;

    @Test
    void getCompilations() throws Exception {
        when(service.getCompilations(false, 0, 10)).thenReturn(List.of(compilation));
        mvc.perform(
                get("/compilations?pinned=false&from=0&size=10")
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(jsonPath("$.[0].id", is(compilation.getId()), Long.class))
            .andExpect(jsonPath("$.[0].title", is(compilation.getTitle())))
            .andExpect(jsonPath("$.[0].pinned", is(compilation.isPinned())))
            .andExpect(jsonPath("$.[0].events[0].id", is(compilation.getEvents().get(0).getId()), Long.class))
            .andExpect(jsonPath("$.[0].events[0].title", is(compilation.getEvents().get(0).getTitle())))
            .andExpect(jsonPath("$.[0].events[0].annotation", is(compilation.getEvents().get(0).getAnnotation())))
            .andExpect(
                jsonPath(
                    "$.[0].events[0].category.id",
                    is(compilation.getEvents().get(0).getCategory().getId()),
                    Long.class
                )
            )
            .andExpect(
                jsonPath("$.[0].events[0].category.name", is(compilation.getEvents().get(0).getCategory().getName()))
            )
            .andExpect(jsonPath("$.[0].events[0].eventDate", is(blinkTime)))
            .andExpect(
                jsonPath(
                    "$.[0].events[0].initiator.id",
                    is(compilation.getEvents().get(0).getInitiator().getId()),
                    Long.class
                )
            )
            .andExpect(
                jsonPath("$.[0].events[0].initiator.name", is(compilation.getEvents().get(0).getInitiator().getName()))
            )
            .andExpect(jsonPath("$.[0].events[0].paid", is(compilation.getEvents().get(0).isPaid())))
            .andExpect(
                jsonPath(
                    "$.[0].events[0].confirmedRequests",
                    is(compilation.getEvents().get(0).getConfirmedRequests()),
                    Long.class
                )
            )
            .andExpect(jsonPath("$.[0].events[0].views", is(compilation.getEvents().get(0).getViews()), Long.class))
            .andExpect(jsonPath("$.[0].events[1].id", is(compilation.getEvents().get(1).getId()), Long.class))
            .andExpect(jsonPath("$.[0].events[1].title", is(compilation.getEvents().get(1).getTitle())))
            .andExpect(jsonPath("$.[0].events[1].annotation", is(compilation.getEvents().get(1).getAnnotation())))
            .andExpect(
                jsonPath(
                    "$.[0].events[1].category.id",
                    is(compilation.getEvents().get(1).getCategory().getId()),
                    Long.class)
            )
            .andExpect(
                jsonPath("$.[0].events[1].category.name", is(compilation.getEvents().get(1).getCategory().getName()))
            )
            .andExpect(jsonPath("$.[0].events[1].eventDate", is(mcrTime)))
            .andExpect(
                jsonPath(
                    "$.[0].events[1].initiator.id",
                    is(compilation.getEvents().get(1).getInitiator().getId()),
                    Long.class
                )
            )
            .andExpect(
                jsonPath("$.[0].events[1].initiator.name", is(compilation.getEvents().get(1).getInitiator().getName()))
            )
            .andExpect(jsonPath("$.[0].events[1].paid", is(compilation.getEvents().get(1).isPaid())))
            .andExpect(
                jsonPath(
                    "$.[0].events[1].confirmedRequests",
                    is(compilation.getEvents().get(1).getConfirmedRequests()),
                    Long.class
                )
            )
            .andExpect(jsonPath("$.[0].events[1].views", is(compilation.getEvents().get(1).getViews()), Long.class));
    }

    @Test
    void getCompilation() throws Exception {
        when(service.getCompilation(1L)).thenReturn(compilation);
        mvc.perform(
                get("/compilations/{compId}", 1L)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(compilation.getId()), Long.class))
            .andExpect(jsonPath("$.title", is(compilation.getTitle())))
            .andExpect(jsonPath("$.pinned", is(compilation.isPinned())))
            .andExpect(jsonPath("$.events[0].id", is(compilation.getEvents().get(0).getId()), Long.class))
            .andExpect(jsonPath("$.events[0].title", is(compilation.getEvents().get(0).getTitle())))
            .andExpect(jsonPath("$.events[0].annotation", is(compilation.getEvents().get(0).getAnnotation())))
            .andExpect(
                jsonPath(
                    "$.events[0].category.id",
                    is(compilation.getEvents().get(0).getCategory().getId()),
                    Long.class
                )
            )
            .andExpect(
                jsonPath("$.events[0].category.name", is(compilation.getEvents().get(0).getCategory().getName()))
            )
            .andExpect(jsonPath("$.events[0].eventDate", is(blinkTime)))
            .andExpect(
                jsonPath(
                    "$.events[0].initiator.id",
                    is(compilation.getEvents().get(0).getInitiator().getId()),
                    Long.class
                )
            )
            .andExpect(
                jsonPath("$.events[0].initiator.name", is(compilation.getEvents().get(0).getInitiator().getName()))
            )
            .andExpect(jsonPath("$.events[0].paid", is(compilation.getEvents().get(0).isPaid())))
            .andExpect(
                jsonPath(
                    "$.events[0].confirmedRequests",
                    is(compilation.getEvents().get(0).getConfirmedRequests()),
                    Long.class
                )
            )
            .andExpect(jsonPath("$.events[0].views", is(compilation.getEvents().get(0).getViews()), Long.class))
            .andExpect(jsonPath("$.events[1].id", is(compilation.getEvents().get(1).getId()), Long.class))
            .andExpect(jsonPath("$.events[1].title", is(compilation.getEvents().get(1).getTitle())))
            .andExpect(jsonPath("$.events[1].annotation", is(compilation.getEvents().get(1).getAnnotation())))
            .andExpect(
                jsonPath(
                    "$.events[1].category.id",
                    is(compilation.getEvents().get(1).getCategory().getId()),
                    Long.class)
            )
            .andExpect(
                jsonPath("$.events[1].category.name", is(compilation.getEvents().get(1).getCategory().getName()))
            )
            .andExpect(jsonPath("$.events[1].eventDate", is(mcrTime)))
            .andExpect(
                jsonPath(
                    "$.events[1].initiator.id",
                    is(compilation.getEvents().get(1).getInitiator().getId()),
                    Long.class
                )
            )
            .andExpect(
                jsonPath("$.events[1].initiator.name", is(compilation.getEvents().get(1).getInitiator().getName()))
            )
            .andExpect(jsonPath("$.events[1].paid", is(compilation.getEvents().get(1).isPaid())))
            .andExpect(
                jsonPath(
                    "$.events[1].confirmedRequests",
                    is(compilation.getEvents().get(1).getConfirmedRequests()),
                    Long.class
                )
            )
            .andExpect(jsonPath("$.events[1].views", is(compilation.getEvents().get(1).getViews()), Long.class));
    }
}