package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Utils;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.*;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.enums.EventStateAction;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.handler.NotFoundException;
import ru.practicum.location.service.LocationService;
import ru.practicum.user.service.UserService;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final UserService userService;
    private final CategoryService categoryService;
    private final LocationService locationService;
    private final EventRepository repository;
    private final EventMapper eventMapper;
    private final CategoryMapper categoryMapper;

    public EventFullDto getById(@NotNull Long id) {
        var event = findById(id);
        return eventMapper.toFullDto(event);
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
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDto create(@PathVariable Long userId,
                               @RequestBody NewEventDto dto) {

        var initiator = userService.getById(userId);
        var location = locationService.create(dto.getLocation());
        var categoryId = dto.getCategory();
        var category = categoryMapper.toCategory(categoryId, categoryService.getById(categoryId));
        var event = repository.save(eventMapper.toEvent(dto, category, initiator, location));

        return eventMapper.toFullDto(event);
    }

    public EventFullDto getById(@NotNull Long userId,
                                @NotNull Long eventId) {
        userService.getById(userId);

        var message = "Event with id=" + eventId + " was not found";

        var event = repository.findById(eventId).orElseThrow(
                () -> new NotFoundException(message)); //TODO дописать

        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException(message);
        }

        return eventMapper.toFullDto(event);
    }

    public EventFullDto updateByIdAndUserId(@NotNull Long userId,
                                            @NotNull Long eventId,
                                            @NotNull UpdateEventUserRequest request) {
        userService.getById(userId);

        var event = findById(eventId);
        var eventState = event.getState();

        if (!eventState.equals(EventState.CANCELED) && !eventState.equals(EventState.PENDING)) {
            throw new UnsupportedOperationException("Only pending or canceled events can be changed");
        }

        var requestDateRaw = request.getEventDate();
        if (requestDateRaw != null) {
            var eventDate = LocalDateTime.parse(requestDateRaw, Utils.dateTimeFormatter);

            var isNotNewDateAfterNextTwoHours = LocalDateTime.now().plusHours(2).isAfter(eventDate);
            var isDateNotEquals = !event.getEventDate().equals(eventDate);

            if (isNotNewDateAfterNextTwoHours && isDateNotEquals) {
                throw new UnsupportedOperationException("DEBUG TODO 99"); // TODO найти правильную фразу
            }

            event.setEventDate(eventDate);
        }

        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }

        var categoryId = request.getCategory();
        if (categoryId != null && !categoryId.equals(event.getCategory().getId())) {
            var category = categoryMapper.toCategory(categoryId, categoryService.getById(categoryId));
            event.setCategory(category);
        }

        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }

        if (request.getLocation() != null) {
            var lat = request.getLocation().getLat();
            var lon = request.getLocation().getLon();
            var location = locationService.getByLatAndLon(lat, lon);

            if (!event.getLocation().getId().equals(location.getId())) {
                event.setLocation(location);
            }
        }

        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }

        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }

        if (request.getStateAction().equals(EventStateAction.CANCEL_REVIEW)) {
            event.setState(EventState.CANCELED);
        } else {
            event.setState(EventState.PENDING);
        }

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        event = repository.save(event);
        return eventMapper.toFullDto(event);
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
