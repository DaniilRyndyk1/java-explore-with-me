package ru.practicum.statservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.statservice.dto.EndpointHitInputDto;
import ru.practicum.statservice.dto.EndpointHitResultDto;
import ru.practicum.statservice.mapper.EndpointHitMapper;
import ru.practicum.statservice.repository.EndpointHitRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EndpointHitService {

    private final EndpointHitRepository repository;
    private final EndpointHitMapper mapper;

    public List<EndpointHitResultDto> getAllByParams(LocalDateTime start,
                                                     LocalDateTime end,
                                                     String[] uris,
                                                     Boolean unique) {

        if (end.isBefore(start)) {
            throw new DateTimeParseException("", null, 0);
        }

        if (uris == null) {
            if (unique) {
                return repository.findUniqueByPeriod(start, end);
            }
            return repository.findByPeriod(start, end);
        }

        if (unique) {
            return repository.findUniqueByPeriodAndUriIn(start, end, uris);
        }
        return repository.findByPeriodAndUriIn(start, end, uris);
    }

    @Transactional
    public void create(EndpointHitInputDto dto) {
        var item = mapper.toEndpointHit(dto);
        repository.save(item);
    }
}
