package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.event.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CompilationDto {

    @NotNull
    private Long id;
    private List<EventShortDto> events;

    @NotNull
    private Boolean pinned;

    @NotBlank
    private String title;
}
