package ru.practicum.location.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.model.Location;

@Component
public class LocationMapper {
    public LocationDto toDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }

    public Location toLocation(LocationDto dto) {
        return new Location(-1L, dto.getLat(), dto.getLon());
    }
}
