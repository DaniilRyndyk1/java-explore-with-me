package ru.practicum.compilation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.handler.EntityNotFoundException;
import ru.practicum.location.model.Location;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.event.TestData.*;

@WebMvcTest(CompilationController.class)
public class CompilationControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CompilationService service;
    @Autowired
    private MockMvc mvc;

    private Compilation compilation;
    private CompilationDto compilationDto;
    private NewCompilationDto newCompilationDto;
    private EventShortDto shortDto;

    @BeforeEach
    void setUp() {
        var event = new Event(
                -1L,
                annotation,
                new Category(-1L, "best"),
                0L,
                LocalDateTime.now(),
                description,
                eventDate,
                new User(-1L, "test", "test@ya.ru"),
                new Location(-1L, locationDto.getLat(), locationDto.getLon()),
                paid,
                0L,
                publishedOn,
                false,
                state,
                title,
                0L,
                null
        );

        compilation = new Compilation(
                1L,
                Set.of(event),
                "best activities",
                false
        );

        shortDto = new EventShortDto(
                event.getId(),
                annotation,
                new CategoryDto(-1L, "best"),
                0L,
                eventDateRaw,
                new UserShortDto(-1L, "test"),
                paid,
                title,
                0L
        );

        compilationDto = new CompilationDto(
                compilation.getId(),
                Set.of(shortDto),
                compilation.getTitle(),
                compilation.getPinned()
        );

        newCompilationDto = new NewCompilationDto(
                Set.of(1L),
                compilation.getPinned(),
                compilation.getTitle()
        );
    }

    @Test
    void shouldGetById() throws Exception {
        when(service.getDtoById(any(Long.class))).thenReturn(compilationDto);

        mvc.perform(get("/compilations/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(compilation.getId()), Long.class))
                .andExpect(jsonPath("$.events.[0].id", is(shortDto.getId()), Long.class))
                .andExpect(jsonPath("$.title", is(compilation.getTitle())))
                .andExpect(jsonPath("$.pinned", is(compilation.getPinned())));
    }

    @Test
    void shouldNotGetByIdWithNotFoundId() throws Exception {
        when(service.getDtoById(any(Long.class)))
                .thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/compilations/999").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.reason", is("The required object was not found.")));
    }

    @Test
    void shouldGetAll() throws Exception {
        when(service.getAll(any(Boolean.class), any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(compilationDto));

        mvc.perform(get("/compilations?pinned=false").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(compilation.getId()), Long.class))
                .andExpect(jsonPath("$.[0].events.[0].id", is(shortDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].title", is(compilation.getTitle())))
                .andExpect(jsonPath("$.[0].pinned", is(compilation.getPinned())));
    }

    @Test
    void shouldCreate() throws Exception {
        when(service.add(any(NewCompilationDto.class))).thenReturn(compilationDto);

        mvc.perform(post("/admin/compilations")
                        .content(mapper.writeValueAsString(newCompilationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(compilation.getId()), Long.class))
                .andExpect(jsonPath("$.events.[0].id", is(shortDto.getId()), Long.class))
                .andExpect(jsonPath("$.title", is(compilation.getTitle())))
                .andExpect(jsonPath("$.pinned", is(compilation.getPinned())));
    }

    @Test
    void shouldDelete() throws Exception {
        mvc.perform(delete("/admin/compilations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldPatch() throws Exception {
        when(service.update(any(Long.class), any(UpdateCompilationRequest.class)))
                .thenReturn(compilationDto);

        mvc.perform(patch("/admin/compilations/1")
                        .content(mapper.writeValueAsString(new UpdateCompilationRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(compilation.getId()), Long.class))
                .andExpect(jsonPath("$.events.[0].id", is(shortDto.getId()), Long.class))
                .andExpect(jsonPath("$.title", is(compilation.getTitle())))
                .andExpect(jsonPath("$.pinned", is(compilation.getPinned())));
    }
}