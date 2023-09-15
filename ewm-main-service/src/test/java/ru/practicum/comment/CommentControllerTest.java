package ru.practicum.comment;

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
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentAdminRequest;
import ru.practicum.comment.dto.UpdateCommentUserRequest;
import ru.practicum.comment.enums.CommentStatus;
import ru.practicum.comment.service.CommentService;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.handler.EntityNotFoundException;
import ru.practicum.location.model.Location;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.event.TestData.*;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CommentService service;
    @Autowired
    private MockMvc mvc;

    private CommentDto commentDto;
    private NewCommentDto newCommentDto;
//    private NewCompilationDto newCompilationDto;
//    private EventShortDto shortDto;

    @BeforeEach
    void setUp() {
        var user = new User(10L, "test", "test@ya.ru");

        var event = new Event(
                15L,
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
                0L
        );

        var shortDto = new EventShortDto(
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

        newCommentDto = new NewCommentDto("test", 2);

        commentDto = new CommentDto(
                2L,
                shortDto,
                new UserShortDto(user.getId(), user.getName()),
                newCommentDto.getText(),
                newCommentDto.getRating(),
                CommentStatus.REJECTED
        );
    }

    @Test
    void shouldGetById() throws Exception {
        when(service.getDtoByIds(any(Long.class), any(Long.class))).thenReturn(commentDto);

        mvc.perform(get("/users/1/comments/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.event.id", is(commentDto.getEvent().getId()), Long.class))
                .andExpect(jsonPath("$.user.id", is(commentDto.getUser().getId()), Long.class))
                .andExpect(jsonPath("$.user.name", is(commentDto.getUser().getName())))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.rating", is(commentDto.getRating()), Integer.class))
                .andExpect(jsonPath("$.status", is(commentDto.getStatus().name())));
    }

    @Test
    void shouldNotGetByIdWithNotFoundId() throws Exception {
        when(service.getDtoByIds(any(Long.class), any(Long.class)))
                .thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/users/1/comments/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.reason", is("The required object was not found.")));
    }

    @Test
    void shouldNotGetByIdWithNotFoundUserId() throws Exception {
        when(service.getDtoByIds(any(Long.class), any(Long.class)))
                .thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/users/999/comments/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.reason", is("The required object was not found.")));
    }

    @Test
    void shouldGetAllByUserId() throws Exception {
        when(service.getAllByUser(any(Long.class), any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(commentDto));

        mvc.perform(get("/users/1/comments").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].event.id", is(commentDto.getEvent().getId()), Long.class))
                .andExpect(jsonPath("$.[0].user.id", is(commentDto.getUser().getId()), Long.class))
                .andExpect(jsonPath("$.[0].user.name", is(commentDto.getUser().getName())))
                .andExpect(jsonPath("$.[0].text", is(commentDto.getText())))
                .andExpect(jsonPath("$.[0].rating", is(commentDto.getRating()), Integer.class))
                .andExpect(jsonPath("$.[0].status", is(commentDto.getStatus().name())));
    }

    @Test
    void shouldNotGetAllWithNotFoundUserId() throws Exception {
        when(service.getAllByUser(any(Long.class), any(Integer.class), any(Integer.class)))
                .thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/users/1/comments").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.reason", is("The required object was not found.")));
    }

    @Test
    void shouldGetAllByEventId() throws Exception {
        when(service.getAllByEvent(any(Long.class), any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(commentDto));

        mvc.perform(get("/events/1/comments").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].event.id", is(commentDto.getEvent().getId()), Long.class))
                .andExpect(jsonPath("$.[0].user.id", is(commentDto.getUser().getId()), Long.class))
                .andExpect(jsonPath("$.[0].user.name", is(commentDto.getUser().getName())))
                .andExpect(jsonPath("$.[0].text", is(commentDto.getText())))
                .andExpect(jsonPath("$.[0].rating", is(commentDto.getRating()), Integer.class))
                .andExpect(jsonPath("$.[0].status", is(commentDto.getStatus().name())));
    }

    @Test
    void shouldNotGetAllWithNotFoundEventId() throws Exception {
        when(service.getAllByEvent(any(Long.class), any(Integer.class), any(Integer.class)))
                .thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/events/1/comments").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.reason", is("The required object was not found.")));
    }

    @Test
    void shouldCreate() throws Exception {
        when(service.create(any(Long.class), any(Long.class), any(NewCommentDto.class)))
                .thenReturn(commentDto);

        mvc.perform(post("/events/1/comments/1")
                        .content(mapper.writeValueAsString(newCommentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.event.id", is(commentDto.getEvent().getId()), Long.class))
                .andExpect(jsonPath("$.user.id", is(commentDto.getUser().getId()), Long.class))
                .andExpect(jsonPath("$.user.name", is(commentDto.getUser().getName())))
                .andExpect(jsonPath("$.text", is(newCommentDto.getText())))
                .andExpect(jsonPath("$.rating", is(newCommentDto.getRating()), Integer.class))
                .andExpect(jsonPath("$.status", is(commentDto.getStatus().name())));
    }

    @Test
    void shouldDelete() throws Exception {
        mvc.perform(delete("/events/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldUpdateByUser() throws Exception {
        when(service.update(any(Long.class), any(Long.class), any(UpdateCommentUserRequest.class)))
                .thenReturn(commentDto);

        mvc.perform(patch("/events/1/comments/1")
                        .content(mapper.writeValueAsString(new UpdateCommentUserRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.event.id", is(commentDto.getEvent().getId()), Long.class))
                .andExpect(jsonPath("$.user.id", is(commentDto.getUser().getId()), Long.class))
                .andExpect(jsonPath("$.user.name", is(commentDto.getUser().getName())))
                .andExpect(jsonPath("$.text", is(newCommentDto.getText())))
                .andExpect(jsonPath("$.rating", is(newCommentDto.getRating()), Integer.class))
                .andExpect(jsonPath("$.status", is(commentDto.getStatus().name())));
    }

    @Test
    void shouldUpdateByAdmin() throws Exception {
        when(service.updateByAdmin(any(Long.class), any(Long.class), any(UpdateCommentAdminRequest.class)))
                .thenReturn(commentDto);

        mvc.perform(patch("/admin/events/1/comments/1")
                        .content(mapper.writeValueAsString(new UpdateCommentAdminRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.event.id", is(commentDto.getEvent().getId()), Long.class))
                .andExpect(jsonPath("$.user.id", is(commentDto.getUser().getId()), Long.class))
                .andExpect(jsonPath("$.user.name", is(commentDto.getUser().getName())))
                .andExpect(jsonPath("$.text", is(newCommentDto.getText())))
                .andExpect(jsonPath("$.rating", is(newCommentDto.getRating()), Integer.class))
                .andExpect(jsonPath("$.status", is(commentDto.getStatus().name())));
    }
}