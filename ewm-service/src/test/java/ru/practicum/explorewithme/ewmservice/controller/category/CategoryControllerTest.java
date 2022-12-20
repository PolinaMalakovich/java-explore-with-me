package ru.practicum.explorewithme.ewmservice.controller.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.ewmservice.service.category.CategoryService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest {
    private final CategoryDto category = new CategoryDto(1L, "Concerts");
    @Autowired
    ObjectMapper mapper;

    @MockBean
    CategoryService service;

    @Autowired
    private MockMvc mvc;

    @Test
    void getCategories() throws Exception {
        when(service.getCategories(0, 10)).thenReturn(List.of(category));
        mvc.perform(
                get("/categories")
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id", is(category.getId()), Long.class))
            .andExpect(jsonPath("$.[0].name", is(category.getName())));
    }

    @Test
    void getCategory() throws Exception {
        when(service.getCategory(category.getId())).thenReturn(category);
        mvc.perform(
                get("/categories/{categoryId}", category.getId())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(category.getId()), Long.class))
            .andExpect(jsonPath("$.name", is(category.getName())));
    }
}