package ru.practicum.location.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.location.dto.LocationDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class LocationMapperTest {
    private final LocationMapper mapper;
    private final Float lat = 15f;
    private final Float lon = 15f;
    private final LocationDto locationDto = new LocationDto(lat, lon);

    @Test
    void shouldConvertLocationDtoToLocation() {
        var result = mapper.toLocation(locationDto);
        assertEquals(result.getLat(), locationDto.getLat());
        assertEquals(result.getLon(), locationDto.getLon());
    }
}
