package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
public class UserShortDto {
    @NotNull
    private Long id;

    @Size(max = 250, min = 2)
    @NotBlank
    private String name;
}