package ru.practicum.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CategoryService service;
    @Autowired
    private MockMvc mvc;

    private final NewCategoryDto newCategoryDto = new NewCategoryDto("Test");
    private final CategoryDto categoryDto = new CategoryDto(0L, "Test");
    private final CategoryDto categoryDto2 = new CategoryDto(0L, "Test2");

    @Test
    void shouldCreate() throws Exception {
        when(service.create(any(NewCategoryDto.class)))
                .thenReturn(categoryDto);

        mvc.perform(post("/admin/categories")
                        .content(mapper.writeValueAsString(newCategoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(categoryDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(categoryDto.getName())));
    }

    @Test
    void shouldRemove() throws Exception {
        mvc.perform(delete("/admin/categories/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldUpdate() throws Exception {
        when(service.update(any(Long.class), any(CategoryDto.class)))
                .thenReturn(categoryDto);
        mvc.perform(patch("/admin/categories/1")
                        .content(mapper.writeValueAsString(categoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(categoryDto.getName())));
    }

    @Test
    void shouldGetAll() throws Exception {
        when(service.getAll(any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(categoryDto, categoryDto2));

        mvc.perform(get("/categories").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(categoryDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(categoryDto.getName())))
                .andExpect(jsonPath("$.[1].id", is(categoryDto2.getId()), Long.class))
                .andExpect(jsonPath("$.[1].name", is(categoryDto2.getName())));
    }

    @Test
    void shouldGetById() throws Exception {
        when(service.getDtoById(any(Long.class)))
                .thenReturn(categoryDto);

        mvc.perform(get("/categories/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(categoryDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(categoryDto.getName())));
    }
}
