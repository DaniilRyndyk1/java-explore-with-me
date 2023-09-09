package ru.practicum.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CategoryDto {

    private Long id;

    @Size(min = 1, max = 50)
    @NotNull
    @NotBlank
    private String name;
}
