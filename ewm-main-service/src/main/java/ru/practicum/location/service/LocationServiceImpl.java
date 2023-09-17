package ru.practicum.location.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;

@Primary
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationMapper mapper;
    private final LocationRepository repository;

    public Location getById(Long id, Float lat, Float lon) {
        return repository.findById(id).orElseGet(
                () -> getByLatAndLon(lat, lon));
    }

    public Location getByLatAndLon(Float lat, Float lon) {
        return repository.findOneByLatAndLon(lat, lon).orElseGet(
                () -> create(new LocationDto(lat, lon)));
    }

    @Transactional
    public Location create(LocationDto dto) {
        return repository.save(mapper.toLocation(dto));
    }
}
