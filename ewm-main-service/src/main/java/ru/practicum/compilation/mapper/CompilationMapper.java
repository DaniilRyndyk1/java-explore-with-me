package ru.practicum.compilation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
//    public Compilation toCompilation(CompilationDto dto, Set<Event> events) {
//        return new Compilation(
//                dto.getId(),
//                events,
//                dto.getTitle(),
//                dto.getPinned()
//        );
//    }

    public Compilation toCompilation(NewCompilationDto dto, Set<Event> events) {
        return new Compilation(
                -1L,
                events,
                dto.getTitle(),
                dto.getPinned()
        );
    }

    public CompilationDto toDto(Compilation compilation, Set<EventShortDto> events) {
        return new CompilationDto(
                compilation.getId(),
                events,
                compilation.getTitle(),
                compilation.getPinned()
        );
    }
}
