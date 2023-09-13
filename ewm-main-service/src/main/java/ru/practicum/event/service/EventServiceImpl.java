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
import ru.practicum.handler.ConflictException;
import ru.practicum.handler.NotFoundException;
import ru.practicum.handler.ValidationException;
import ru.practicum.location.service.LocationService;
import ru.practicum.statservice.StatClient;
import ru.practicum.statservice.dto.EndpointHitInputDto;
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
    private final StatClient client;

    public EventFullDto getDtoById(@NotNull Long id) {
        var event = getById(id);

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Event with id=" + id + " was not found");
        }

        setViews(List.of(event));

        return eventMapper.toFullDto(getById(id));
    }

    public Event getById(@NotNull Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Event with id=" + id + " was not found")
        );
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

        var eventDate = LocalDateTime.parse(dto.getEventDate(), dateTimeFormatter);

        if (LocalDateTime.now().isAfter(eventDate)) {
            throw new ValidationException("Incorrect date");
        }

        var event = eventMapper.toEvent(dto, category, user, location);
        event.setState(EventState.PENDING);
        event.setViews(0L);
        event.setCreatedOn(LocalDateTime.now());
        event.setConfirmedRequests(0L);

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
            throw new ConflictException("Cannot publish the event because it's not in the right state: " + state);
        }

        if (state.equals(EventState.PUBLISHED) && stateAction.equals(EventStateAction.CANCEL_REVIEW)) {
            throw new UnsupportedOperationException("Cannot cancel the event because it's not in the right state: " + state);
        }


        event.setEventDate(
                getUpdateRequestCorrectDate(
                        request,
                        event.getEventDate(),
                        1L
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
                        2L
                        // TODO найти правильную фразу
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

        var start = LocalDateTime.now();
        var end = start.plusYears(10000);
        if (rangeStart != null && rangeEnd != null) {
            start = LocalDateTime.parse(rangeStart, dateTimeFormatter);
            end = LocalDateTime.parse(rangeEnd, dateTimeFormatter);
        }

        var events = repository.findAllByAdminParams(
                users,
                states,
                categories,
                start,
                end,
                users == null ? 0 : users.length,
                states == null ? 0 : states.length,
                categories == null ? 0 : categories.length,
                pageRequest)
                .stream()
                .collect(Collectors.toList());


        setViews(events);

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
                                      @NotNull Integer size,
                                      @NotNull String ip) {

        var pageRequest = Utils.getPageRequest(from, size);
        var start = LocalDateTime.now();
        var end = start.plusYears(10000);

        if (rangeStart != null && rangeEnd != null) {
            start = LocalDateTime.parse(rangeStart, dateTimeFormatter);
            end = LocalDateTime.parse(rangeEnd, dateTimeFormatter);
            if (end.isBefore(start)) {
                throw new ValidationException("End date mush be after start date");
            }
        }

        var events = repository.findAllByUserParams(
                text,
                categories,
                paid,
                start,
                end,
                paid == null,
                text == null,
                categories == null? 0 : categories.length,
                pageRequest).stream().collect(Collectors.toList());

        events.forEach(x -> client.createHit(new EndpointHitInputDto(
                "ewn",
                "/events/" + x.getId(),
                ip,
                LocalDateTime.now().format(dateTimeFormatter)
        )));

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

        setViews(events);

        return events
                .stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    public void setViews(@NotNull List<Event> events) {
        for (Event event : events) {
            var hits = client.getHitsWithParams(
                    LocalDateTime.now().minusHours(100),
                    LocalDateTime.now().plusHours(100),
                    new String[] {"/events/" + event.getId()},
                    true
            );

            var views = hits.size();

            repository.setViewsById(
                    event.getId(),
                    (long) views
            );
        }
    }

    public Set<Event> getAllByIds(Set<Long> ids) {
        var result = new HashSet<Event>();
        for (Long id : ids) {
            result.add(getById(id));
        }
        return result;
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

        if (stateAction != null) {
            if (stateAction.equals(EventStateAction.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            } else if (stateAction.equals(EventStateAction.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
            } else {
                event.setState(EventState.PENDING);
            }
        }

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        return repository.save(event);
    }

    private LocalDateTime getUpdateRequestCorrectDate(UpdateEventUserRequest request,
                                                      LocalDateTime eventDate,
                                                      Long hours) {
        var dateRaw = request.getEventDate();
        if (dateRaw != null) {
            var date = LocalDateTime.parse(dateRaw, dateTimeFormatter);

            if (LocalDateTime.now().plusHours(hours).isAfter(date)) {
                throw new ValidationException("Incorrect date");
            }

            return date;
        }
        return eventDate;
    }
}
