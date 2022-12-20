package ru.practicum.explorewithme.ewmservice.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.ewmservice.service.user.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    private final UserDto alice = new UserDto(1L, "Alice", "alice.anderson@example.com");

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService service;

    @Autowired
    private MockMvc mvc;

    @Test
    void getUsers() throws Exception {
        when(service.getUsers(List.of(1L), 0, 10)).thenReturn(List.of(alice));

        mvc.perform(
                get("/admin/users?ids=1")
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id", is(alice.getId()), Long.class))
            .andExpect(jsonPath("$.[0].name", is(alice.getName())))
            .andExpect(jsonPath("$.[0].email", is(alice.getEmail())));
    }

    @Test
    void addUser() throws Exception {
        when(service.addUser(any())).thenReturn(alice);

        mvc.perform(
                post("/admin/users")
                    .content(mapper.writeValueAsString(alice))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(alice.getId()), Long.class))
            .andExpect(jsonPath("$.name", is(alice.getName())))
            .andExpect(jsonPath("$.email", is(alice.getEmail())));
    }

    @Test
    void deleteUser() throws Exception {
        mvc.perform(
                delete("/admin/users/{userId}", alice.getId())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());

        verify(service, times(1)).deleteUser(anyLong());
    }
}