package ru.practicum.participationRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.participationRequest.dto.EventRequestStatusUpdateRequest;
import ru.practicum.participationRequest.dto.EventRequestStatusUpdateResult;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.service.ParticipationRequestService;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ParticipationRequestController {
    private final ParticipationRequestService service;

    @GetMapping("users/{userId}/events/{eventId}/requests")
    public ParticipationRequestDto getRequest(@PathVariable Long userId,
                                              @PathVariable Long eventId) {
        return service.getByIds(userId, eventId);
    }

    @PatchMapping("users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestsStatus(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @RequestBody EventRequestStatusUpdateRequest request) {
        return service.changeRequestsStatus(userId, eventId, request);
    }
}
