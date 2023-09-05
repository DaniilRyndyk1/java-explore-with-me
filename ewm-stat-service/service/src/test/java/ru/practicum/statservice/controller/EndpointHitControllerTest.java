package ru.practicum.statservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.statservice.dto.EndpointHitInputDto;
import ru.practicum.statservice.dto.EndpointHitResultDto;
import ru.practicum.statservice.service.EndpointHitService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EndpointHitController.class)
public class EndpointHitControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private EndpointHitService service;

    @Autowired
    private MockMvc mvc;

    private final EndpointHitInputDto hitInputDto = new EndpointHitInputDto(
            "ewm-main-service",
            "/events/2",
            "192.163.0.1",
            "2023-06-07 11:00:23"
    );

    private final EndpointHitResultDto hitResultDto = new EndpointHitResultDto(
            "ewm-main-service",
            "/events/2",
            1L
    );

    @Test
    void shouldCreateHit() throws Exception {
        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(hitInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetAllByParams() throws Exception {
        when(service.getAllByParams(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(String[].class),
                any(Boolean.class)))
                .thenReturn(List.of(hitResultDto));
        mvc.perform(get("/stats?start=2023-01-01 00:00:00&end=2023-12-31 23:59:59&uris=/events/2&unique=false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].app", is(hitResultDto.getApp())))
                .andExpect(jsonPath("$.[0].uri", is(hitResultDto.getUri())))
                .andExpect(jsonPath("$.[0].hits", is(1)));
    }

    @Test
    void shouldGetAllByParamsWithoutUnique() throws Exception {
        when(service.getAllByParams(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(String[].class),
                any()))
                .thenReturn(List.of(hitResultDto));
        mvc.perform(get("/stats?start=2023-01-01 00:00:00&end=2023-12-31 23:59:59&uris=/events/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].app", is(hitResultDto.getApp())))
                .andExpect(jsonPath("$.[0].uri", is(hitResultDto.getUri())))
                .andExpect(jsonPath("$.[0].hits", is(1)));
    }

    @Test
    void shouldGetAllByParamsWithoutUniqueAndUris() throws Exception {
        when(service.getAllByParams(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(),
                any()))
                .thenReturn(List.of(hitResultDto));
        mvc.perform(get("/stats?start=2023-01-01 00:00:00&end=2023-12-31 23:59:59"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].app", is(hitResultDto.getApp())))
                .andExpect(jsonPath("$.[0].uri", is(hitResultDto.getUri())))
                .andExpect(jsonPath("$.[0].hits", is(1)));
    }

    @Test
    void shouldNotGetAllByParamsWithoutStart() throws Exception {
        mvc.perform(get("/stats?end=2023-12-31 23:59:59"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotGetAllByParamsWithoutEnd() throws Exception {
        mvc.perform(get("/stats?start=2023-12-31 23:59:59"))
                .andExpect(status().isBadRequest());
    }
}

