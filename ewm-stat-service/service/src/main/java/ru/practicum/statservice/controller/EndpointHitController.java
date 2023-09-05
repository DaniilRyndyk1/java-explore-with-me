package ru.practicum.statservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statservice.dto.EndpointHitInputDto;
import ru.practicum.statservice.dto.EndpointHitResultDto;
import ru.practicum.statservice.service.EndpointHitService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class EndpointHitController {
    private final EndpointHitService service;
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("stats")
    public List<EndpointHitResultDto> getAll(@RequestParam("start") String startDateString,
                                             @RequestParam("end") String endDateString,
                                             @RequestParam(required = false) String[] uris,
                                             @RequestParam(required = false) Boolean unique) {

        var startDate = LocalDateTime.parse(startDateString, formatter);
        var endDate = LocalDateTime.parse(endDateString, formatter);

        return service.getAllByParams(startDate, endDate, uris, unique);
    }

    @PostMapping("hit")
    public ResponseEntity create(@RequestBody EndpointHitInputDto dto) {
        service.create(dto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}

