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

    @GetMapping("users/{userId}/events")
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        throw new RuntimeException("Метод не реализован");
    }

    @PostMapping("users/{userId}/events")
    public EventFullDto addUserEvent(@PathVariable Long userId,
                                     @RequestBody NewEventDto dto) {
        throw new RuntimeException("Метод не реализован");
    }

    @GetMapping("users/{userId}/events/{eventId}")
    public EventFullDto getUserEventById(@PathVariable Long userId,
                                         @PathVariable Long eventId) {
        throw new RuntimeException("Метод не реализован");
    }

    @PatchMapping("users/{userId}/events/{eventId}")
    public EventFullDto updateUserEventById(@PathVariable Long userId,
                                            @PathVariable Long eventId,
                                            @RequestBody UpdateEventUserRequest request) {
        throw new RuntimeException("Метод не реализован");
    }

    @GetMapping("users/{userId}/events/{eventId}/requests")
    public ParticipationRequestDto getUserEventRequests(@PathVariable Long userId,
                                                        @PathVariable Long eventId) {
        throw new RuntimeException("Метод не реализован");
    }

    @PatchMapping("users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeUserEventRequestStatus(@PathVariable Long userId,
                                                                       @PathVariable Long eventId,
                                                                       @RequestBody EventRequestStatusUpdateRequest request) {
        throw new RuntimeException("Метод не реализован");
    }

    @GetMapping("admin/events")
    public List<EventFullDto> searchEvents(@RequestParam Integer[] users,
                                           @RequestParam String[] states,
                                           @RequestParam Integer[] categories,
                                           @RequestParam String rangeStart,
                                           @RequestParam String rangeEnd,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        throw new RuntimeException("Метод не реализован");
    }

    @PatchMapping("admin/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @RequestBody UpdateEventAdminRequest request) {
        throw new RuntimeException("Метод не реализован");
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
        throw new RuntimeException("Метод не реализован");
    }

    @GetMapping("events/{id}")
    public EventFullDto getById(@PathVariable Integer id) {
        throw new RuntimeException("Метод не реализован");
    }
}