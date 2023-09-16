package ru.practicum.compilation.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class CompilationMapperTest {
    private final CompilationMapper mapper;

    private final Set<Event> events = Set.of(new Event(), new Event(), new Event());
    private final Set<EventShortDto> shortEvents = Set.of(new EventShortDto(), new EventShortDto(), new EventShortDto());

    private final NewCompilationDto newCompilationDto = new NewCompilationDto(
            Set.of(1L, 2L, 3L),
            false,
            "best activities"
    );

    private final Compilation compilation = new Compilation(
            1L,
            events,
            "best activities",
            false
    );

    @Test
    void shouldConvertNewCompilationDtoToCompilation() {
        var result = mapper.toCompilation(newCompilationDto, events);
        assertNotNull(result);
        assertEquals(result.getTitle(), newCompilationDto.getTitle());
        assertEquals(result.getPinned(), newCompilationDto.getPinned());
        assertEquals(result.getEvents().size(), 3);
        assertEquals(result.getEvents(), events);
    }

    @Test
    void shouldConvertCompilationToCompilationDto() {
        var result = mapper.toDto(compilation, shortEvents);
        assertNotNull(result);
        assertEquals(result.getId(), compilation.getId());
        assertEquals(result.getPinned(), compilation.getPinned());
        assertEquals(result.getTitle(), compilation.getTitle());
        assertEquals(result.getEvents(), shortEvents);
    }
}
