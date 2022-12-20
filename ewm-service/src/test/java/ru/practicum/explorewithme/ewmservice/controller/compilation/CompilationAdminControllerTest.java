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
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;
import ru.practicum.explorewithme.ewmservice.service.compilation.CompilationService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CompilationAdminController.class)
class CompilationAdminControllerTest {
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
    private final NewCompilationDto newCompilation = new NewCompilationDto("Reunion Concerts", false, Set.of(1L, 2L));
    private final CompilationDto compilation = new CompilationDto(
        1L,
        newCompilation.getTitle(),
        newCompilation.isPinned(),
        List.of(blink128, mcr)
    );

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CompilationService service;

    @Autowired
    private MockMvc mvc;

    @Test
    void addCompilation() throws Exception {
        when(service.addCompilation(any())).thenReturn(compilation);
        mvc.perform(
                post("/admin/compilations")
                    .content(mapper.writeValueAsString(newCompilation))
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

    @Test
    void deleteCompilation() throws Exception {
        service.deleteCompilation(compilation.getId());
        verify(service, times(1)).deleteCompilation(anyLong());
        mvc.perform(
                delete("/admin/compilations/{compId}", compilation.getId())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    void deleteEventFromCompilation() throws Exception {
        mvc.perform(
                delete("/admin/compilations/{compId}/events/{eventId}", compilation.getId(), mcr.getId())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());

        verify(service, times(1)).deleteEventFromCompilation(anyLong(), anyLong());

    }

    @Test
    void addEventToCompilation() throws Exception {
        final EventShortDto fellsilent = new EventShortDto(
            2L,
            "Fellsilent",
            "Fellsilent reunion concert",
            category,
            LocalDateTime.of(2023, 2, 21, 21, 0, 0),
            gerard,
            true,
            0L,
            0L
        );
        final String fellsilentTime = "2023-02-21 21:00:00";
        final CompilationDto updatedCompilation = new CompilationDto(
            1L,
            newCompilation.getTitle(),
            newCompilation.isPinned(),
            List.of(blink128, mcr, fellsilent)
        );
        when(service.addEventToCompilation(compilation.getId(), fellsilent.getId())).thenReturn(updatedCompilation);
        mvc.perform(
                patch("/admin/compilations/{compId}/events/{eventId}", compilation.getId(), fellsilent.getId())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(updatedCompilation.getId()), Long.class))
            .andExpect(jsonPath("$.title", is(updatedCompilation.getTitle())))
            .andExpect(jsonPath("$.pinned", is(updatedCompilation.isPinned())))
            .andExpect(jsonPath("$.events[0].id", is(updatedCompilation.getEvents().get(0).getId()), Long.class))
            .andExpect(jsonPath("$.events[0].title", is(updatedCompilation.getEvents().get(0).getTitle())))
            .andExpect(jsonPath("$.events[0].annotation", is(updatedCompilation.getEvents().get(0).getAnnotation())))
            .andExpect(
                jsonPath(
                    "$.events[0].category.id",
                    is(updatedCompilation.getEvents().get(0).getCategory().getId()),
                    Long.class
                )
            )
            .andExpect(
                jsonPath("$.events[0].category.name", is(updatedCompilation.getEvents().get(0).getCategory().getName()))
            )
            .andExpect(jsonPath("$.events[0].eventDate", is(blinkTime)))
            .andExpect(
                jsonPath(
                    "$.events[0].initiator.id",
                    is(updatedCompilation.getEvents().get(0).getInitiator().getId()),
                    Long.class
                )
            )
            .andExpect(
                jsonPath(
                    "$.events[0].initiator.name",
                    is(updatedCompilation.getEvents().get(0).getInitiator().getName())
                )
            )
            .andExpect(jsonPath("$.events[0].paid", is(updatedCompilation.getEvents().get(0).isPaid())))
            .andExpect(
                jsonPath(
                    "$.events[0].confirmedRequests",
                    is(updatedCompilation.getEvents().get(0).getConfirmedRequests()),
                    Long.class
                )
            )
            .andExpect(jsonPath("$.events[0].views", is(updatedCompilation.getEvents().get(0).getViews()), Long.class))
            .andExpect(jsonPath("$.events[1].id", is(updatedCompilation.getEvents().get(1).getId()), Long.class))
            .andExpect(jsonPath("$.events[1].title", is(updatedCompilation.getEvents().get(1).getTitle())))
            .andExpect(jsonPath("$.events[1].annotation", is(updatedCompilation.getEvents().get(1).getAnnotation())))
            .andExpect(
                jsonPath(
                    "$.events[1].category.id",
                    is(updatedCompilation.getEvents().get(1).getCategory().getId()),
                    Long.class)
            )
            .andExpect(
                jsonPath("$.events[1].category.name", is(updatedCompilation.getEvents().get(1).getCategory().getName()))
            )
            .andExpect(jsonPath("$.events[1].eventDate", is(mcrTime)))
            .andExpect(
                jsonPath(
                    "$.events[1].initiator.id",
                    is(updatedCompilation.getEvents().get(1).getInitiator().getId()),
                    Long.class
                )
            )
            .andExpect(
                jsonPath("$.events[1].initiator.name",
                    is(updatedCompilation.getEvents().get(1).getInitiator().getName()))
            )
            .andExpect(jsonPath("$.events[1].paid", is(updatedCompilation.getEvents().get(1).isPaid())))
            .andExpect(
                jsonPath(
                    "$.events[1].confirmedRequests",
                    is(updatedCompilation.getEvents().get(1).getConfirmedRequests()),
                    Long.class
                )
            )
            .andExpect(jsonPath("$.events[1].views", is(updatedCompilation.getEvents().get(1).getViews()), Long.class))


            .andExpect(jsonPath("$.events[2].id", is(updatedCompilation.getEvents().get(2).getId()), Long.class))
            .andExpect(jsonPath("$.events[2].title", is(updatedCompilation.getEvents().get(2).getTitle())))
            .andExpect(jsonPath("$.events[2].annotation", is(updatedCompilation.getEvents().get(2).getAnnotation())))
            .andExpect(
                jsonPath(
                    "$.events[2].category.id",
                    is(updatedCompilation.getEvents().get(2).getCategory().getId()),
                    Long.class)
            )
            .andExpect(
                jsonPath("$.events[2].category.name", is(updatedCompilation.getEvents().get(2).getCategory().getName()))
            )
            .andExpect(jsonPath("$.events[2].eventDate", is(fellsilentTime)))
            .andExpect(
                jsonPath(
                    "$.events[2].initiator.id",
                    is(updatedCompilation.getEvents().get(2).getInitiator().getId()),
                    Long.class
                )
            )
            .andExpect(
                jsonPath("$.events[2].initiator.name",
                    is(updatedCompilation.getEvents().get(2).getInitiator().getName()))
            )
            .andExpect(jsonPath("$.events[2].paid", is(updatedCompilation.getEvents().get(2).isPaid())))
            .andExpect(
                jsonPath(
                    "$.events[2].confirmedRequests",
                    is(updatedCompilation.getEvents().get(2).getConfirmedRequests()),
                    Long.class
                )
            )
            .andExpect(jsonPath("$.events[2].views", is(updatedCompilation.getEvents().get(2).getViews()), Long.class));
    }

    @Test
    void unpinCompilation() throws Exception {
        mvc.perform(
                delete("/admin/compilations/{compId}/pin", compilation.getId())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());

        verify(service, times(1)).unpinCompilation(anyLong());
    }

    @Test
    void pinCompilation() throws Exception {
        mvc.perform(
                patch("/admin/compilations/{compId}/pin", compilation.getId())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());

        verify(service, times(1)).pinCompilation(anyLong());
    }
}