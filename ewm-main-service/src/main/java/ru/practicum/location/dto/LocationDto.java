package ru.practicum.location.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class LocationDto {
    @NotNull
    private float lat;

    @NotNull
    private float lon;
}