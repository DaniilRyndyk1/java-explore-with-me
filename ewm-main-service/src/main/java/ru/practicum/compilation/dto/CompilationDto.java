package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.event.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CompilationDto {

    @NotNull
    private Long id;
    private Set<EventShortDto> events;

    @NotBlank
    private String title;

    @NotNull
    private Boolean pinned;
}
