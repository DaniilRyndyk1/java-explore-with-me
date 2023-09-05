package ru.practicum.statservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EndpointHitResultDto {
    private String app;
    private String uri;
    private Long hits;
}