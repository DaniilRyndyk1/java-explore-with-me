package ru.practicum.location.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    @NotNull
    private float lat;

    @NotNull
    private float lon;
}