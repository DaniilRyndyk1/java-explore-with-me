package ru.practicum.statservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EndpointHitInputDto {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
