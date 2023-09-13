package ru.practicum.participationRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.Utils;
import ru.practicum.category.model.Category;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.event.model.Event;
import ru.practicum.location.model.Location;
import ru.practicum.participationRequest.dto.EventRequestStatusUpdateRequest;
import ru.practicum.participationRequest.dto.EventRequestStatusUpdateResult;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.enums.ParticipationRequestState;
import ru.practicum.participationRequest.model.ParticipationRequest;
import ru.practicum.participationRequest.service.ParticipationRequestService;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.event.TestData.*;

@WebMvcTest(ParticipationRequestController.class)
public class ParticipationRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ParticipationRequestService service;
    @Autowired
    private MockMvc mvc;

    private ParticipationRequestDto requestDto;

    @BeforeEach
    void setUp() {
        var event = new Event(
                2L,
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

        User user = new User(
                1L,
                "best activities",
                "bbbbb@ya.ru"
        );

        ParticipationRequest request = new ParticipationRequest(
                1L,
                LocalDateTime.now(),
                event,
                user,
                ParticipationRequestState.PENDING
        );

        requestDto = new ParticipationRequestDto(
                request.getId(),
                request.getCreated().format(Utils.dateTimeFormatter),
                event.getId(),
                user.getId(),
                request.getStatus()
        );
    }

    @Test
    void shouldUpdateRequestsStatuses() throws Exception {
        var result = new EventRequestStatusUpdateResult(
                List.of(),
                List.of(requestDto)
        );

        when(service.changeRequestsStatus(any(Long.class), any(Long.class), any(EventRequestStatusUpdateRequest.class)))
                .thenReturn(result);

        mvc.perform(patch("/users/1/events/2/requests")
                        .content(mapper.writeValueAsString(new UpdateCompilationRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rejectedRequests.[0].id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.rejectedRequests.[0].created", is(requestDto.getCreated())))
                .andExpect(jsonPath("$.rejectedRequests.[0].event", is(requestDto.getEvent()), Long.class))
                .andExpect(jsonPath("$.rejectedRequests.[0].requester", is(requestDto.getRequester()), Long.class))
                .andExpect(jsonPath("$.rejectedRequests.[0].status", is(requestDto.getStatus().name())));
    }

    @Test
    void shouldGetAllByUser() throws Exception {
        when(service.getAll(any(Long.class))).thenReturn(List.of(requestDto));

        mvc.perform(get("/users/1/requests").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].created", is(requestDto.getCreated())))
                .andExpect(jsonPath("$.[0].event", is(requestDto.getEvent()), Long.class))
                .andExpect(jsonPath("$.[0].requester", is(requestDto.getRequester()), Long.class))
                .andExpect(jsonPath("$.[0].status", is(requestDto.getStatus().name())));
    }

    @Test
    void shouldCreate() throws Exception {
        when(service.create(any(Long.class), any(Long.class))).thenReturn(requestDto);

        mvc.perform(post("/users/1/requests?userId=1&eventId=2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.created", is(requestDto.getCreated())))
                .andExpect(jsonPath("$.event", is(requestDto.getEvent()), Long.class))
                .andExpect(jsonPath("$.requester", is(requestDto.getRequester()), Long.class))
                .andExpect(jsonPath("$.status", is(requestDto.getStatus().name())));
    }

    @Test
    void shouldPatch() throws Exception {
        when(service.cancel(any(Long.class), any(Long.class))).thenReturn(requestDto);

        mvc.perform(patch("/users/1/requests/2/cancel").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.created", is(requestDto.getCreated())))
                .andExpect(jsonPath("$.event", is(requestDto.getEvent()), Long.class))
                .andExpect(jsonPath("$.requester", is(requestDto.getRequester()), Long.class))
                .andExpect(jsonPath("$.status", is(requestDto.getStatus().name())));
    }
}