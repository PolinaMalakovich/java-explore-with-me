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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryAdminController.class)
class CategoryAdminControllerTest {
    private final CategoryDto category = new CategoryDto(1L, "Concerts");
    @Autowired
    ObjectMapper mapper;

    @MockBean
    CategoryService service;

    @Autowired
    private MockMvc mvc;

    @Test
    void updateCategory() throws Exception {
        when(service.updateCategory(any())).thenReturn(category);
        mvc.perform(
                patch("/admin/categories")
                    .content(mapper.writeValueAsString(category))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(category.getId()), Long.class))
            .andExpect(jsonPath("$.name", is(category.getName())));
    }

    @Test
    void addCategory() throws Exception {
        when(service.addCategory(any())).thenReturn(category);
        mvc.perform(
                post("/admin/categories")
                    .content(mapper.writeValueAsString(category))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(category.getId()), Long.class))
            .andExpect(jsonPath("$.name", is(category.getName())));
    }

    @Test
    void deleteCategory() throws Exception {
        mvc.perform(
                delete("/admin/categories/{categoryId}", category.getId())
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());

        verify(service, times(1)).deleteCategory(anyLong());
    }
}