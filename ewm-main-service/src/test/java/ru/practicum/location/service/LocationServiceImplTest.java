package ru.practicum.location.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.model.Location;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class LocationServiceImplTest {
    private final LocationServiceImpl service;
    private final LocationDto locationDto = new LocationDto(15, 15);
    private final LocationDto locationDto2 = new LocationDto(25, 25);

    private Location location1;

    @BeforeEach
    void setup() {
        location1 = service.create(locationDto);
    }

    @Test
    void shouldGetByLatAndLon() {
        var original = service.create(locationDto2);
        var location = service.getByLatAndLon(25f, 25f);
        assertNotNull(location);
        assertEquals(original.getLat(), location.getLat());
        assertEquals(original.getLon(), location.getLon());
    }

    @Test
    void shouldGetById() {
        var location = service.getById(location1.getId(), 25f, 25f);
        assertNotNull(location);
        assertEquals(location1.getLat(), location.getLat());
        assertEquals(location1.getLon(), location.getLon());
    }

    @Test
    void shouldCreate() {
        var location = service.create(locationDto2);
        assertNotNull(location);
        assertEquals(locationDto2.getLat(), location.getLat());
        assertEquals(locationDto2.getLon(), location.getLon());
    }

    @Test
    void shouldCreateWhenGetByLatAndLonIsEmpty() {
        var lat = 50f;
        var lon = 10f;
        var location = service.getByLatAndLon(lat, lon);
        assertNotNull(location);
        assertEquals(location1.getId() + 1, location.getId());
        assertEquals(lat, location.getLat());
        assertEquals(lon, location.getLon());
    }

    @Test
    void shouldCreateWhenGetByIdIsEmpty() {
        var lat = 50f;
        var lon = 10f;
        var newId = location1.getId() + 1;
        var location = service.getById(newId, lat, lon);
        assertNotNull(location);
        assertEquals(newId, location.getId());
        assertEquals(lat, location.getLat());
        assertEquals(lon, location.getLon());
    }
}