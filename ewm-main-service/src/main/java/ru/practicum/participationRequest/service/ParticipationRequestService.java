package ru.practicum.participationRequest.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.participationRequest.dto.EventRequestStatusUpdateRequest;
import ru.practicum.participationRequest.dto.EventRequestStatusUpdateResult;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ParticipationRequestService {
    List<ParticipationRequestDto> getAll(@PathVariable Long userId);
    ParticipationRequestDto create(@PathVariable Long userId,
                                   @RequestParam Long eventId);
    ParticipationRequestDto cancel(@PathVariable Long userId,
                                   @PathVariable Long requestId);
    ParticipationRequestDto getDtoById(@NotNull Long userId,
                                       @NotNull Long eventId);
    EventRequestStatusUpdateResult changeRequestsStatus(@NotNull Long userId,
                                                        @NotNull Long eventId,
                                                        @NotNull EventRequestStatusUpdateRequest request);

    List<ParticipationRequestDto> getAllByInitiatorIdAndEventId(@NotNull Long userId, @NotNull Long eventId);
}
