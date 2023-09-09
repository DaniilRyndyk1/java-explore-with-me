package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.*;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.handler.NotFoundException;
import ru.practicum.user.dto.ParticipationRequestDto;
import ru.practicum.user.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final UserService userService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final EventRepository repository;
    private final EventMapper mapper;

    public EventFullDto getById(@NotNull Long id) {
        var event = findById(id);
        return mapper.toFullDto(event);
    }

    public List<EventShortDto> getAllByUser(@NotNull Long userId,
                                            @NotNull Integer from,
                                            @NotNull Integer size) {
        userService.getById(userId);

        if (from < 0 || size <= 0) {
            throw new IllegalArgumentException("Неверное значение параметра");
        }

        return repository.findAllByInitiator_Id(userId, PageRequest.of(from / size, size))
                .stream()
                .map(mapper::toShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDto create(@PathVariable Long userId,
                               @RequestBody NewEventDto dto) {
        userService.getById(userId);

        var event = mapper.toEvent(dto, )
        return repository.save(event);
    }

    public EventFullDto getById(@NotNull Long userId,
                                         @NotNull Long eventId) {
        throw new RuntimeException("Метод не реализован");
    }

    public EventFullDto updateByIdAndUserId(@NotNull Long userId,
                                            @NotNull Long eventId,
                                            @NotNull UpdateEventUserRequest request) {
        throw new RuntimeException("Метод не реализован");
    }

    public ParticipationRequestDto getEventRequestsByUserId(@NotNull Long userId,
                                                            @NotNull Long eventId) {
        throw new RuntimeException("Метод не реализован");
    }

    public EventRequestStatusUpdateResult changeEventStatusByUserId(@NotNull Long userId,
                                                                    @NotNull Long eventId,
                                                                    @NotNull EventRequestStatusUpdateRequest request) {
        throw new RuntimeException("Метод не реализован");
    }

    public List<EventFullDto> search(Integer[] users,
                                     String[] states,
                                     Integer[] categories,
                                     String rangeStart,
                                     String rangeEnd,
                                     @NotNull Integer from,
                                     @NotNull Integer size) {
        throw new RuntimeException("Метод не реализован");
    }

    public EventFullDto update(@NotNull Long eventId, @NotNull UpdateEventAdminRequest request) {
        throw new RuntimeException("Метод не реализован");
    }

    public List<EventShortDto> getAll(String text,
                                      Integer[] categories,
                                      Boolean paid,
                                      String rangeStart,
                                      String rangeEnd,
                                      Boolean onlyAvailable,
                                      String sort,
                                      @NotNull Integer from,
                                      @NotNull Integer size) {
        throw new RuntimeException("Метод не реализован");
    }

    private Event findById(@NotNull Long id) {
        var eventOptional = repository.findById(id);
        if (eventOptional.isEmpty()) {
            throw new NotFoundException("Event with id=" + id + " was not found");
        }
        return eventOptional.get();
    }
}
