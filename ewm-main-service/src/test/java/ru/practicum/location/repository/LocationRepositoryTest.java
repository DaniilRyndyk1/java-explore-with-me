package ru.practicum.location.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.location.model.Location;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase
public class LocationRepositoryTest {
    @Autowired
    private LocationRepository repository;
    private final Location location =  new Location(-1L, 15f, 15f);

    @BeforeEach
    void setUp() {
        repository.save(location);
    }

    @Test
    void shouldFindLocationByLatAndLon() {
        var result = repository.findOneByLatAndLon(location.getLat(), location.getLon());
        assertTrue(result.isPresent());
        assertEquals(location.getLat(), result.get().getLat());
        assertEquals(location.getLon(), result.get().getLon());
    }
}