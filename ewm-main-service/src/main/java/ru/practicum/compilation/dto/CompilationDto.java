package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.event.dto.EventShortDto;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CompilationDto {
    private Long id;
    private Set<EventShortDto> events;
    private String title;
    private Boolean pinned;
}
