package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;
import ru.practicum.user.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class EventController {
    private final EventService service;

    @GetMapping("events/{id}")
    public EventFullDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("users/{userId}/events")
    public List<EventShortDto> getAllByUser(@PathVariable Long userId,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        return service.getAllByUser(userId, from, size);
    }

    @PostMapping("users/{userId}/events")
    public EventFullDto create(@PathVariable Long userId,
                               @RequestBody NewEventDto dto) {
        return service.create(userId, dto);
    }

    @GetMapping("users/{userId}/events/{eventId}")
    public EventFullDto getById(@PathVariable Long userId,
                                @PathVariable Long eventId) {
        return service.getById(userId, eventId);
    }

    @PatchMapping("users/{userId}/events/{eventId}")
    public EventFullDto updateByIdAndUserId(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @RequestBody UpdateEventUserRequest request) {
        return service.updateByIdAndUserId(userId, eventId, request);
    }

    @GetMapping("users/{userId}/events/{eventId}/requests")
    public ParticipationRequestDto getEventRequestsByUserId(@PathVariable Long userId,
                                                            @PathVariable Long eventId) {
        return service.getEventRequestsByUserId(userId, eventId);
    }

    @PatchMapping("users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeEventStatusByUserId(@PathVariable Long userId,
                                                                    @PathVariable Long eventId,
                                                                    @RequestBody EventRequestStatusUpdateRequest request) {
        return service.changeEventStatusByUserId(userId, eventId, request);
    }

    @GetMapping("admin/events")
    public List<EventFullDto> search(@RequestParam Integer[] users,
                                     @RequestParam String[] states,
                                     @RequestParam Integer[] categories,
                                     @RequestParam String rangeStart,
                                     @RequestParam String rangeEnd,
                                     @RequestParam(defaultValue = "0") Integer from,
                                     @RequestParam(defaultValue = "10") Integer size) {
        return service.search(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("admin/events/{eventId}")
    public EventFullDto update(@PathVariable Long eventId,
                               @RequestBody UpdateEventAdminRequest request) {
        return service.update(eventId, request);
    }

    @GetMapping("events")
    public List<EventShortDto> getAll(@RequestParam String text,
                                      @RequestParam Integer[] categories,
                                      @RequestParam Boolean paid,
                                      @RequestParam String rangeStart,
                                      @RequestParam String rangeEnd,
                                      @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                      @RequestParam String sort,
                                      @RequestParam(defaultValue = "0") Integer from,
                                      @RequestParam(defaultValue = "10") Integer size) {
        return service.getAll(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }
}