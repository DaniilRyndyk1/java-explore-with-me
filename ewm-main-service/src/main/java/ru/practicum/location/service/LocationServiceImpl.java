package ru.practicum.location.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.location.repository.LocationRepository;

@Primary
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationMapper mapper;
    private final LocationRepository repository;

    public LocationDto getById(Long id) {
        return null;
    }

    public LocationDto create(LocationDto dto) {
        var location = mapper.toLocation(dto);
        return mapper.toDto(repository.save(location));
    }
}
