package ru.practicum.compilation.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.Utils;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.event.TestData.*;


@DataJpaTest
@AutoConfigureTestDatabase
public class CompilationRepositoryTest {
    @Autowired
    private CompilationRepository repository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserRepository userRepository;

    private Compilation compilation;
    private Event event;

    @BeforeEach
    void setUp() {
        var location = new Location(-1L, locationDto.getLat(), locationDto.getLon());
        location = locationRepository.save(location);

        var category = new Category(-1L, "best");
        category = categoryRepository.save(category);

        var user = new User(-1L, "test", "test@ya.ru");
        user = userRepository.save(user);

        event = eventRepository.save(
                new Event(
                        -1L,
                        annotation,
                        category,
                        0L,
                        LocalDateTime.now(),
                        description,
                        eventDate,
                        user,
                        location,
                        paid,
                        0L,
                        publishedOn,
                        false,
                        state,
                        title,
                        0L
                )
        );

        compilation = repository.save(
                new Compilation(
                        1L,
                        Set.of(event),
                        "best activities",
                        false
                )
        );
    }

    @Test
    void shouldFindAllByPinned() {
        var pageable = Utils.getPageRequest(0, 10);
        var result = repository.findAllByPinned(false, pageable).stream().collect(Collectors.toList());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(compilation.getId(), result.get(0).getId());
        assertEquals(compilation.getTitle(), result.get(0).getTitle());
        assertEquals(compilation.getPinned(), result.get(0).getPinned());
        assertEquals(1, result.get(0).getEvents().size());
        assertEquals(event.getId(), result.get(0).getId());
    }
}