package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.Utils;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.handler.NotFoundException;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final EventService eventService;

    public CompilationDto getDtoById(@NotNull Long id) {
        var compilation = getById(id);

        var events = compilation
                .getEvents()
                .stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toSet());

        return compilationMapper.toDto(compilation, events);
    }

    public Compilation getById(@NotNull Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Compilation with id=" + id + " was not found")
        );
    }

    public CompilationDto add(@NotNull NewCompilationDto dto) {

        Set<Event> events = Set.of();
        if (dto.getEvents() != null) {
            events = eventService.getAllByIds(dto.getEvents());
        }

        var compilation = compilationMapper.toCompilation(dto, events);

        return compilationMapper.toDto(
                repository.save(compilation),
                events
                        .stream()
                        .map(eventMapper::toShortDto)
                        .collect(Collectors.toSet())
        );
    }

    public void delete(@NotNull Long id) {
        var compilation = getById(id);
        repository.delete(compilation);
    }

    public CompilationDto update(@NotNull Long id,
                                 @NotNull UpdateCompilationRequest request) {
        getById(id);
        var events = eventService.getAllByIds(request.getEvents());

        var compilation = new Compilation(
                id,
                events,
                request.getTitle(),
                request.getPinned()
        );

        return compilationMapper.toDto(
                compilation,
                events.stream().map(eventMapper::toShortDto).collect(Collectors.toSet())
        );
    }

    public List<CompilationDto> getAll(@NotNull Boolean pinned,
                                       @NotNull Integer from,
                                       @NotNull Integer size) {
        var pageRequest = Utils.getPageRequest(from, size);

        Page<Compilation> compilations;

        if (pinned == null) {
            compilations = repository.findAll(pageRequest);
        } else {
            compilations = repository.findAllByPinned(pinned, pageRequest);
        }

        return compilations
                .stream()
                .map(x -> compilationMapper.toDto(x, x
                        .getEvents()
                        .stream()
                        .map(eventMapper::toShortDto)
                        .collect(Collectors.toSet())))
                .collect(Collectors.toList());
    }
}