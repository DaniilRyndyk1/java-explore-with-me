package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.service.EventService;
import ru.practicum.statservice.StatClient;
import ru.practicum.statservice.dto.EndpointHitInputDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.Utils.dateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class EventController {
    private final EventService service;
    private final StatClient client;

    @GetMapping("events/{id}")
    public EventFullDto getById(@PathVariable Long id,
                                HttpServletRequest request) {
        createHit(request);
        return service.getDtoById(id);
    }

    @GetMapping("users/{userId}/events")
    public List<EventShortDto> getAllByUser(@PathVariable Long userId,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        return service.getAllByUser(userId, from, size);
    }

    @PostMapping("users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable Long userId,
                               @Valid @RequestBody NewEventDto dto) {
        return service.create(userId, dto);
    }

    @GetMapping("users/{userId}/events/{eventId}")
    public EventFullDto getById(@PathVariable Long userId,
                                @PathVariable Long eventId) {
        return service.getDtoById(userId, eventId);
    }

    @PatchMapping("users/{userId}/events/{eventId}")
    public EventFullDto updateByIdAndUserId(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @Valid @RequestBody UpdateEventUserRequest request) {
        return service.update(userId, eventId, request);
    }

    @GetMapping("admin/events")
    public List<EventFullDto> search(@RequestParam(required = false) List<Long> users,
                                     @RequestParam(required = false) List<EventState> states,
                                     @RequestParam(required = false) List<Long> categories,
                                     @RequestParam(required = false) String rangeStart,
                                     @RequestParam(required = false) String rangeEnd,
                                     @RequestParam(defaultValue = "0") Integer from,
                                     @RequestParam(defaultValue = "10") Integer size) {
        System.out.println("TEST");
        return service.search(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("admin/events/{eventId}")
    public EventFullDto update(@PathVariable Long eventId,
                               @Valid @RequestBody UpdateEventAdminRequest request) {
        return service.update(eventId, request);
    }

    @GetMapping("events")
    public List<EventShortDto> getAll(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) List<Long> categories,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(required = false) String rangeStart,
                                      @RequestParam(required = false) String rangeEnd,
                                      @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                      @RequestParam(required = false) String sort,
                                      @RequestParam(defaultValue = "0") Integer from,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      HttpServletRequest request) {
        createHit(request);
        return service.getAll(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size,
                request.getRemoteAddr());
    }

    private void createHit(HttpServletRequest request) {
        client.createHit(
                new EndpointHitInputDto(
                        "ewm",
                        request.getRequestURI(),
                        request.getRemoteAddr(),
                        LocalDateTime.now().format(dateTimeFormatter)
                )
        );
    }
}