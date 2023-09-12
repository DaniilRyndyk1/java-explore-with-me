package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
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
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.Utils.dateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final UserService userService;
    private final CategoryService categoryService;
    private final LocationService locationService;
    private final EventRepository repository;
    private final EventMapper eventMapper;
    private final CategoryMapper categoryMapper;

    public EventFullDto getDtoById(@NotNull Long id) {
        var event = getById(id);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Event with id=" + id + " was not found");
        }
        return eventMapper.toFullDto(event);
    }

    public Event getById(@NotNull Long id) {
        var event = repository.findById(id).orElseThrow(
                () -> new NotFoundException("Event with id=" + id + " was not found")
        );
        incrementViews(List.of(event));
        return event;
    }

    public List<EventShortDto> getAllByUser(@NotNull Long userId,
                                            @NotNull Integer from,
                                            @NotNull Integer size) {
        userService.getById(userId);

        var pageRequest = Utils.getPageRequest(from, size);

        return repository.findAllByInitiator_Id(userId, pageRequest)
                .stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDto create(@PathVariable Long userId,
                               @RequestBody NewEventDto dto) {

        var user = userService.getById(userId);
        var location = locationService.create(dto.getLocation());
        var categoryId = dto.getCategory();

        var category = categoryMapper.toCategory(
                categoryId,
                categoryService.getDtoById(categoryId)
        );

        var event = eventMapper.toEvent(dto, category, user, location);
        event.setState(EventState.PENDING);
        event.setViews(0L);

        return eventMapper.toFullDto(
                repository.save(event)
        );
    }

    public EventFullDto getDtoById(@NotNull Long userId,
                                   @NotNull Long id) {
        userService.getById(userId);

        var event = getById(id);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event with id=" + id + " was not found");
        }

        return eventMapper.toFullDto(event);
    }

    public EventFullDto update(@NotNull Long id,
                               @NotNull UpdateEventAdminRequest request) {

        var event = getById(id);
        var state = event.getState();
        var stateAction = request.getStateAction();

        if (!state.equals(EventState.PENDING) && stateAction.equals(EventStateAction.PUBLISH_EVENT)) {
            throw new UnsupportedOperationException("Cannot publish the event because it's not in the right state: " + state);
        }

        if (state.equals(EventState.PUBLISHED) && stateAction.equals(EventStateAction.CANCEL_REVIEW)) {
            throw new UnsupportedOperationException("Cannot cancel the event because it's not in the right state: " + state);
        }

        event.setPublishedOn(
                getUpdateRequestCorrectDate(
                        request,
                        event.getEventDate(),
                        1L,
                        "DEBUG TODO 99" // TODO найти правильную фразу
                )
        );

        return eventMapper.toFullDto(
                setValuesFromRequest(request, event)
        );
    }

    public EventFullDto update(@NotNull Long userId,
                               @NotNull Long id,
                               @NotNull UpdateEventUserRequest request) {
        userService.getById(userId);

        var event = getById(id);
        var state = event.getState();

        if (!state.equals(EventState.CANCELED) && !state.equals(EventState.PENDING)) {
            throw new UnsupportedOperationException("Only pending or canceled events can be changed");
        }

        event.setEventDate(
                getUpdateRequestCorrectDate(
                        request,
                        event.getEventDate(),
                        2L,
                        "DEBUG TODO 99" // TODO найти правильную фразу
                )
        );
        
        return eventMapper.toFullDto(
                setValuesFromRequest(request, event)
        );
    }

    public List<EventFullDto> search(Long[] users,
                                     EventState[] states,
                                     Long[] categories,
                                     String rangeStart,
                                     String rangeEnd,
                                     @NotNull Integer from,
                                     @NotNull Integer size) {

        var pageRequest = Utils.getPageRequest(from, size);
        var start = LocalDateTime.parse(rangeStart, dateTimeFormatter);
        var end = LocalDateTime.parse(rangeEnd, dateTimeFormatter);

        var events = repository.findAllByAdminParams(
                users,
                states,
                categories,
                start,
                end,
                pageRequest)
                .stream()
                .collect(Collectors.toList());


        incrementViews(events);

        return events
                .stream()
                .map(eventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    public List<EventShortDto> getAll(String text,
                                      Long[] categories,
                                      Boolean paid,
                                      String rangeStart,
                                      String rangeEnd,
                                      Boolean onlyAvailable,
                                      String sort,
                                      @NotNull Integer from,
                                      @NotNull Integer size) {

        var pageRequest = Utils.getPageRequest(from, size);
        var start = LocalDateTime.now();
        var end = start.plusYears(10000);

        if (rangeStart != null && rangeEnd != null) {
            start = LocalDateTime.parse(rangeStart, dateTimeFormatter);
            end = LocalDateTime.parse(rangeEnd, dateTimeFormatter);
        }

        var events = repository.findAllByUserParams(
                text,
                categories,
                paid,
                start,
                end,
                pageRequest).toList();

        if (onlyAvailable != null && onlyAvailable) {
            events = events
                    .stream()
                    .filter(x -> x.getParticipantLimit() - x.getConfirmedRequests() > 0)
                    .collect(Collectors.toList());
        }

        if (sort != null) {
            switch (sort) {
                case "EVENT_DATE":
                    events.sort(Comparator.comparing(Event::getEventDate));
                    break;
                case "VIEWS":
                    events.sort(Comparator.comparing(Event::getViews));
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported sort type");
            }
        }

        incrementViews(events);

        return events
                .stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    public void incrementViews(@NotNull List<Event> events) {
        repository.incrementViewsByIds(
                events
                        .stream()
                        .mapToLong(Event::getId)
                        .toArray()
        );
    }

    public Set<Event> getAllByIds(Set<Long> ids) {
        return new HashSet<>(repository.findAllById(ids));
    }

    private Event setValuesFromRequest(UpdateEventUserRequest request, Event event) {
        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }

        var categoryId = request.getCategory();
        if (categoryId != null && !event.getCategory().getId().equals(categoryId)) {
            var category = categoryService.getDtoById(categoryId);
            event.setCategory(
                    categoryMapper.toCategory(categoryId, category)
            );
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

        var stateAction = request.getStateAction();

        if (stateAction.equals(EventStateAction.CANCEL_REVIEW)) {
            event.setState(EventState.CANCELED);
        } else if (stateAction.equals(EventStateAction.PUBLISH_EVENT)) {
            event.setState(EventState.PUBLISHED);
        } else {
            event.setState(EventState.PENDING);
        } // TODO разобраться

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        return repository.save(event);
    }

    private LocalDateTime getUpdateRequestCorrectDate(UpdateEventUserRequest request,
                                                      LocalDateTime eventDate,
                                                      Long hours,
                                                      String errorMessage) {
        var dateRaw = request.getEventDate();
        if (dateRaw != null) {
            var date = LocalDateTime.parse(dateRaw, dateTimeFormatter);

            if (LocalDateTime.now().plusHours(hours).isAfter(date)) {
                throw new UnsupportedOperationException(errorMessage);
            }

            return date;
        }
        return eventDate;
    }
}
