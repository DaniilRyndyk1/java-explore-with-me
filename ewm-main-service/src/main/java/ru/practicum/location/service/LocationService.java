package ru.practicum.location.service;

import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.model.Location;

public interface LocationService {
    Location getById(Long id, Float lat, Float lon);
    Location create(LocationDto dto);
    Location getByLatAndLon(Float lat, Float lon);
}
