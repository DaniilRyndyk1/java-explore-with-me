package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
public class UserDto {
    private Long id;

    @Size(max = 250, min = 2)
    @NotBlank
    private String name;

    @Email
    @Size(max = 254, min = 6)
    @NotNull
    private String email;
}
