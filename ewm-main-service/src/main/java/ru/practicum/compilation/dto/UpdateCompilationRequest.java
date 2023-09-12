package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Size;
import java.util.Set;

@AllArgsConstructor
@Getter
public class UpdateCompilationRequest {

    private Set<Long> events;
    private Boolean pinned;

    @Size(min = 1, max = 50)
    private String title;
}