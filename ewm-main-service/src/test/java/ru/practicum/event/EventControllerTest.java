package ru.practicum.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.dto.*;
import ru.practicum.event.enums.EventState;
import ru.practicum.handler.NotFoundException;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.model.Location;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;
import ru.practicum.event.service.EventServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.Utils.dateTimeFormatter;
import static ru.practicum.event.TestData.*;

@WebMvcTest(EventController.class)
public class EventControllerTest {
    private final String createdOn = LocalDateTime.now().format(dateTimeFormatter);

    @MockBean
    private UserService userService;
    @MockBean
    private EventServiceImpl service;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private NewEventDto newEventDto;
    private EventFullDto event;
    private User user;

    @BeforeEach
    public void setup() {
        user = new User(1L, newUserRequest.getName(), newUserRequest.getEmail());
        newEventDto = new NewEventDto(
                annotation,
                1L,
                description,
                eventDateRaw,
                locationDto,
                paid,
                participantLimit,
                requestModeration,
                title
        );
        event = new EventFullDto(
                1L,
                annotation,
                new CategoryDto(1L, categoryName),
                0L,
                createdOn,
                description,
                eventDateRaw,
                new UserShortDto(1L, user.getName()),
                new Location(1L, locationDto.getLat(), locationDto.getLon()),
                paid,
                participantLimit,
                publishedOnRaw,
                requestModeration,
                state,
                title,
                0L
        );
    }

    @Test
    void shouldGetById() throws Exception {
        when(service.getDtoById(1L)).thenReturn(event);

        mvc.perform(get("/events/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(event.getId()), Long.class))
                .andExpect(jsonPath("$.annotation", is(event.getAnnotation())))
                .andExpect(jsonPath("$.category.id", is(event.getCategory().getId()), Long.class))
                .andExpect(jsonPath("$.category.name", is(event.getCategory().getName())))
                .andExpect(jsonPath("$.confirmedRequests", is(event.getConfirmedRequests()), Long.class))
                .andExpect(jsonPath("$.createdOn", is(createdOn)))
                .andExpect(jsonPath("$.eventDate", is(event.getEventDate())))
                .andExpect(jsonPath("$.initiator.id", is(event.getInitiator().getId()), Long.class))
                .andExpect(jsonPath("$.initiator.name", is(event.getInitiator().getName())))
                .andExpect(jsonPath("$.location.id", is(event.getLocation().getId()), Long.class))
                .andExpect(jsonPath("$.location.lat", is(event.getLocation().getLat()), Float.class))
                .andExpect(jsonPath("$.location.lon", is(event.getLocation().getLon()), Float.class))
                .andExpect(jsonPath("$.paid", is(event.getPaid())))
                .andExpect(jsonPath("$.participantLimit", is(event.getParticipantLimit()), Long.class))
                .andExpect(jsonPath("$.publishedOn", is(event.getPublishedOn())))
                .andExpect(jsonPath("$.requestModeration", is(event.getRequestModeration())))
                .andExpect(jsonPath("$.state", is("PENDING")))
                .andExpect(jsonPath("$.title", is(event.getTitle())))
                .andExpect(jsonPath("$.views", is(event.getViews()), Long.class));
    }

    @Test
    void shouldNotGetByIdWithWrongId() throws Exception {
        when(service.getDtoById(any(Long.class)))
                .thenThrow(NotFoundException.class);

        mvc.perform(get("/events/999").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.reason", is("The required object was not found.")));
    }

    @Test
    void shouldGetAllByUser() throws Exception {
        var eventShortDto = new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                event.getCategory(),
                event.getConfirmedRequests(),
                event.getEventDate(),
                event.getInitiator(),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );

        when(userService.getById(any(Long.class))).thenReturn(user);
        when(service.getAllByUser(
//                any(String.class),
                any(Long.class),
//                any(Boolean.class),
//                any(String.class),
//                any(String.class),
//                any(Boolean.class),
//                any(String.class),
                any(Integer.class),
                any(Integer.class)))
                .thenReturn(List.of(eventShortDto));

        mvc.perform(get("/users/1/events").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(event.getId()), Long.class))
                .andExpect(jsonPath("$.[0].annotation", is(event.getAnnotation())))
                .andExpect(jsonPath("$.[0].category.id", is(event.getCategory().getId()), Long.class))
                .andExpect(jsonPath("$.[0].confirmedRequests", is(0L), Long.class))
                .andExpect(jsonPath("$.[0].eventDate", is(event.getEventDate())))
                .andExpect(jsonPath("$.[0].initiator.id", is(event.getInitiator().getId()), Long.class))
                .andExpect(jsonPath("$.[0].paid", is(event.getPaid())))
                .andExpect(jsonPath("$.[0].title", is(event.getTitle())))
                .andExpect(jsonPath("$.[0].views", is(0L), Long.class));
    }

    @Test
    void shouldCreate() throws Exception {
        when(service.create(any(Long.class), any()))
                .thenReturn(event);

        var newEventDto = new NewEventDto(
                event.getAnnotation(),
                event.getCategory().getId(),
                event.getDescription(),
                event.getEventDate(),
                new LocationDto(event.getLocation().getLat(), event.getLocation().getLon()),
                event.getPaid(),
                event.getParticipantLimit().longValue(),
                event.getRequestModeration(),
                event.getTitle()
        );

        mvc.perform(post("/users/1/events")
                        .content(mapper.writeValueAsString(newEventDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(event.getId()), Long.class))
                .andExpect(jsonPath("$.annotation", is(event.getAnnotation())))
                .andExpect(jsonPath("$.category.id", is(event.getCategory().getId()), Long.class))
                .andExpect(jsonPath("$.confirmedRequests", is(0L), Long.class))
                .andExpect(jsonPath("$.eventDate", is(event.getEventDate())))
                .andExpect(jsonPath("$.initiator.id", is(event.getInitiator().getId()), Long.class))
                .andExpect(jsonPath("$.paid", is(event.getPaid())))
                .andExpect(jsonPath("$.title", is(event.getTitle())))
                .andExpect(jsonPath("$.views", is(0L), Long.class));
    }

    @Test
    void shouldNotCreateWithNotValidBody() throws Exception {
        mvc.perform(post("/users/1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetByUserIdAndEventId() throws Exception {
        when(service.getDtoById(any(Long.class), any(Long.class))).thenReturn(event);

        mvc.perform(get("/users/1/events/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(event.getId()), Long.class))
                .andExpect(jsonPath("$.annotation", is(event.getAnnotation())))
                .andExpect(jsonPath("$.category.id", is(event.getCategory().getId()), Long.class))
                .andExpect(jsonPath("$.category.name", is(event.getCategory().getName())))
                .andExpect(jsonPath("$.confirmedRequests", is(event.getConfirmedRequests()), Long.class))
                .andExpect(jsonPath("$.createdOn", is(createdOn)))
                .andExpect(jsonPath("$.eventDate", is(event.getEventDate())))
                .andExpect(jsonPath("$.initiator.id", is(event.getInitiator().getId()), Long.class))
                .andExpect(jsonPath("$.initiator.name", is(event.getInitiator().getName())))
                .andExpect(jsonPath("$.location.id", is(event.getLocation().getId()), Long.class))
                .andExpect(jsonPath("$.location.lat", is(event.getLocation().getLat()), Float.class))
                .andExpect(jsonPath("$.location.lon", is(event.getLocation().getLon()), Float.class))
                .andExpect(jsonPath("$.paid", is(event.getPaid())))
                .andExpect(jsonPath("$.participantLimit", is(event.getParticipantLimit()), Long.class))
                .andExpect(jsonPath("$.publishedOn", is(event.getPublishedOn())))
                .andExpect(jsonPath("$.requestModeration", is(event.getRequestModeration())))
                .andExpect(jsonPath("$.state", is("PENDING")))
                .andExpect(jsonPath("$.title", is(event.getTitle())))
                .andExpect(jsonPath("$.views", is(event.getViews()), Long.class));
    }

    @Test
    void shouldNotGetByIdWithWrongUserId() throws Exception {
        when(service.getDtoById(any(Long.class), any(Long.class))).thenThrow(NotFoundException.class);

        mvc.perform(get("/users/1/events/999").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.reason", is("The required object was not found.")));
    }

    @Test
    void shouldUpdateByIds() throws Exception {
        when(service.update(any(Long.class), any(Long.class), any())).thenReturn(event);

        mvc.perform(patch("/users/1/events/1")
                        .content(mapper.writeValueAsString(new UpdateEventUserRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(event.getId()), Long.class))
                .andExpect(jsonPath("$.annotation", is(event.getAnnotation())))
                .andExpect(jsonPath("$.category.id", is(event.getCategory().getId()), Long.class))
                .andExpect(jsonPath("$.confirmedRequests", is(0L), Long.class))
                .andExpect(jsonPath("$.eventDate", is(event.getEventDate())))
                .andExpect(jsonPath("$.initiator.id", is(event.getInitiator().getId()), Long.class))
                .andExpect(jsonPath("$.paid", is(event.getPaid())))
                .andExpect(jsonPath("$.title", is(event.getTitle())))
                .andExpect(jsonPath("$.views", is(0L), Long.class));
    }

    @Test
    void shouldNotUpdateWithNotFoundIds() throws Exception {
        when(service.update(any(Long.class), any(Long.class), any())).thenThrow(NotFoundException.class);

        mvc.perform(patch("/users/1/events/1")
                        .content(mapper.writeValueAsString(newEventDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSearch() throws Exception {
        var eventShortDto = new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                event.getCategory(),
                event.getConfirmedRequests(),
                event.getEventDate(),
                event.getInitiator(),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );

        when(userService.getById(any(Long.class))).thenReturn(user);
        when(service.search(
                any(Long[].class),
                any(EventState[].class),
                any(Long[].class),
                any(String.class),
                any(String.class),
                any(Integer.class),
                any(Integer.class)))
                .thenReturn(List.of(event));

        mvc.perform(get("/admin/events?users=12&states=PUBLISHED&categories=11&rangeStart=2022-01-06%2013%3A30%3A38&rangeEnd=2097-09-06%2013%3A30%3A38&from=0&size=1000").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(event.getId()), Long.class))
                .andExpect(jsonPath("$.[0].annotation", is(event.getAnnotation())))
                .andExpect(jsonPath("$.[0].category.id", is(event.getCategory().getId()), Long.class))
                .andExpect(jsonPath("$.[0].confirmedRequests", is(0L), Long.class))
                .andExpect(jsonPath("$.[0].eventDate", is(event.getEventDate())))
                .andExpect(jsonPath("$.[0].initiator.id", is(event.getInitiator().getId()), Long.class))
                .andExpect(jsonPath("$.[0].paid", is(event.getPaid())))
                .andExpect(jsonPath("$.[0].title", is(event.getTitle())))
                .andExpect(jsonPath("$.[0].views", is(0L), Long.class));
    }

    @Test
    void shouldUpdateByAdmin() throws Exception {
        when(service.update(any(Long.class), any(UpdateEventAdminRequest.class)))
                .thenReturn(event);

        mvc.perform(patch("/admin/events/1")
                        .content(mapper.writeValueAsString(new UpdateEventAdminRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(event.getId()), Long.class))
                .andExpect(jsonPath("$.annotation", is(event.getAnnotation())))
                .andExpect(jsonPath("$.category.id", is(event.getCategory().getId()), Long.class))
                .andExpect(jsonPath("$.confirmedRequests", is(0L), Long.class))
                .andExpect(jsonPath("$.eventDate", is(event.getEventDate())))
                .andExpect(jsonPath("$.initiator.id", is(event.getInitiator().getId()), Long.class))
                .andExpect(jsonPath("$.paid", is(event.getPaid())))
                .andExpect(jsonPath("$.title", is(event.getTitle())))
                .andExpect(jsonPath("$.views", is(0L), Long.class));
    }

    @Test
    void shouldUpdateByAdminWithNotFoundEventId() throws Exception {
        when(service.update(any(Long.class), any(UpdateEventAdminRequest.class)))
                .thenThrow(NotFoundException.class);

        mvc.perform(patch("/admin/events/1")
                        .content(mapper.writeValueAsString(new UpdateEventAdminRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.reason", is("The required object was not found.")));
    }

    @Test
    void shouldGetAll() throws Exception {
        var eventShortDto = new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                event.getCategory(),
                event.getConfirmedRequests(),
                event.getEventDate(),
                event.getInitiator(),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );

        mvc.perform(get("/events").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
