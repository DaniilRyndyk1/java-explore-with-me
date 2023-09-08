package ru.practicum.statservice.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.statservice.dto.EndpointHitInputDto;
import ru.practicum.statservice.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EndpointHitMapper {
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EndpointHit toEndpointHit(EndpointHitInputDto dto) {
        return new EndpointHit(
                -1L,
                dto.getApp(),
                dto.getUri(),
                dto.getIp(),
                LocalDateTime.parse(dto.getTimestamp(), formatter));
    }
}