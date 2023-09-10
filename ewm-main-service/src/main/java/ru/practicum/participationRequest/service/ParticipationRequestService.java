package ru.practicum.participationRequest.service;

import ru.practicum.participationRequest.dto.EventRequestStatusUpdateRequest;
import ru.practicum.participationRequest.dto.EventRequestStatusUpdateResult;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;

import javax.validation.constraints.NotNull;

public interface ParticipationRequestService {
    ParticipationRequestDto getByIds(@NotNull Long userId,
                                     @NotNull Long eventId);
    EventRequestStatusUpdateResult changeRequestsStatus(@NotNull Long userId,
                                                        @NotNull Long eventId,
                                                        @NotNull EventRequestStatusUpdateRequest request);
}
