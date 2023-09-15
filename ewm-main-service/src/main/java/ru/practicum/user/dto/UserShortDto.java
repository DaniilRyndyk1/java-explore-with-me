package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserShortDto {
    private Long id;
    private String name;
}