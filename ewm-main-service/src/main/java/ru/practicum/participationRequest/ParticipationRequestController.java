package ru.practicum.participationRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.participationRequest.dto.EventRequestStatusUpdateRequest;
import ru.practicum.participationRequest.dto.EventRequestStatusUpdateResult;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.service.ParticipationRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ParticipationRequestController {
    private final ParticipationRequestService service;

    @GetMapping("users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByInitiatorAndEvent(@PathVariable Long userId,
                                                                        @PathVariable Long eventId) {
        return service.getAllByInitiatorIdAndEventId(userId, eventId);
    }

    @PatchMapping("users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestsStatus(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @RequestBody EventRequestStatusUpdateRequest request) {
        return service.changeRequestsStatus(userId, eventId, request);
    }

    @GetMapping("users/{userId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId) {
        return service.getAll(userId);
    }

    @PostMapping("users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto add(@PathVariable Long userId,
                                       @RequestParam Long eventId) {
        return service.create(userId, eventId);
    }

    @PatchMapping("users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable Long userId,
                                          @PathVariable Long requestId) {
        return service.cancel(userId, requestId);
    }
}
