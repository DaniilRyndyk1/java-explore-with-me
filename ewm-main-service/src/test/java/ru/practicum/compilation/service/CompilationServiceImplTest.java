package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryServiceImpl;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.service.EventServiceImpl;
import ru.practicum.handler.EntityNotFoundException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.service.UserServiceImpl;

import java.util.Set;

import static ru.practicum.event.TestData.*;
import static ru.practicum.event.TestData.title;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class CompilationServiceImplTest {
    private final CompilationServiceImpl service;
    private final EventServiceImpl eventService;
    private final UserServiceImpl userService;
    private final CategoryServiceImpl categoryService;

    private EventFullDto event2;
    private CompilationDto compilationDto;
    private CompilationDto compilationDto2;

    @BeforeEach
    void setup() {
        var user = userService.create(new NewUserRequest("Danila", "konosuba@ya.ru"));

        userService.create(new NewUserRequest("Nikita", "bebe@ya.ru"));

        userService.create(new NewUserRequest("Nikita2", "bebe2@ya.ru"));

        var categoryDto = new NewCategoryDto("The best");
        CategoryDto category = categoryService.create(categoryDto);

        EventFullDto event = eventService.create(user.getId(),
                new NewEventDto(
                        annotation,
                        category.getId(),
                        description,
                        eventDateRaw,
                        locationDto,
                        paid,
                        3L,
                        true,
                        title
                ));

        event2 = eventService.create(user.getId(),
                new NewEventDto(
                        annotation + "!",
                        category.getId(),
                        description + "!",
                        eventDateRaw,
                        locationDto,
                        paid,
                        3L,
                        true,
                        title + "!"
                ));

        compilationDto = service.create(new NewCompilationDto(
                Set.of(event.getId(), event2.getId()),
                false,
                "super good"
        ));

        compilationDto2 = service.create(new NewCompilationDto(
                Set.of(event2.getId()),
                false,
                "super good 2"
        ));
    }

    @Test
    void shouldGetById() {
        var result = service.getById(compilationDto.getId());
        assertNotNull(result);
        assertEquals(compilationDto.getId(), result.getId());
        assertEquals(compilationDto.getEvents().size(), result.getEvents().size());
        assertEquals(compilationDto.getPinned(), result.getPinned());
        assertEquals(compilationDto.getTitle(), result.getTitle());
    }

    @Test
    void shouldNotGetByIdWithNotFoundId() {
        assertThrows(EntityNotFoundException.class,
                () -> service.getById(compilationDto.getId() - 1));
    }

    @Test
    void shouldGetDtoById() {
        var result = service.getDtoById(compilationDto.getId());
        assertNotNull(result);
        assertEquals(compilationDto.getId(), result.getId());
        assertEquals(compilationDto.getEvents().size(), result.getEvents().size());
        assertEquals(compilationDto.getPinned(), result.getPinned());
        assertEquals(compilationDto.getTitle(), result.getTitle());
    }

    @Test
    void shouldNotGetDtoByIdWithNotFoundId() {
        assertThrows(EntityNotFoundException.class,
                () -> service.getDtoById(compilationDto.getId() - 1));
    }

    @Test
    void shouldCreate() {
        var newCompilation = new NewCompilationDto(
                Set.of(event2.getId()),
                false,
                "best 2"
        );
        var result = service.create(newCompilation);
        assertNotNull(result);
        assertEquals(newCompilation.getEvents().size(), result.getEvents().size());
        assertEquals(newCompilation.getPinned(), result.getPinned());
        assertEquals(newCompilation.getTitle(), result.getTitle());
    }

    @Test
    void shouldDelete() {
        var id = compilationDto.getId();
        service.delete(id);
        assertThrows(EntityNotFoundException.class, () -> service.getById(id));
    }

    @Test
    void shouldUpdate() {
        var request = new UpdateCompilationRequest(Set.of(event2.getId()), false, "best 2");
        var result = service.update(compilationDto.getId(), request);

        assertNotNull(result);
        assertEquals(request.getEvents().size(), result.getEvents().size());
        assertEquals(request.getPinned(), result.getPinned());
        assertEquals(request.getTitle(), result.getTitle());
    }

    @Test
    void shouldNotUpdateWithNotFoundId() {
        var request = new UpdateCompilationRequest(Set.of(event2.getId()), false, "best 2");

        assertThrows(EntityNotFoundException.class,
                () -> service.update(compilationDto.getId() + 999L, request));
    }

    @Test
    void shouldNotUpdateWithNotFoundEventId() {
        var request = new UpdateCompilationRequest(
                Set.of(event2.getId() + 999L),
                false,
                "best 2");

        assertThrows(EntityNotFoundException.class,
                () -> service.update(compilationDto.getId(), request));
    }

    @Test
    void shouldGetAll() {
        var result = service.getAll(false, 0, 10);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(compilationDto.getId(), result.get(0).getId());
        assertEquals(compilationDto2.getId(), result.get(1).getId());
    }

    @Test
    void shouldNotGetAllWithNegativeFrom() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getAll(false, -1, 10));
    }

    @Test
    void shouldNotGetAllWithZeroSize() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getAll(false, 0, 0));
    }

    @Test
    void shouldNotGetAllWithNegativeSize() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getAll(false, 1, -10));
    }
}