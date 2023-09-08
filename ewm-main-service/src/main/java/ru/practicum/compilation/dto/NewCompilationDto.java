package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NewCompilationDto {

    private List<Long> events;

    private Boolean pinned = false;

    @Size(min = 1, max = 50)
    @NotBlank
    private String title;
}
