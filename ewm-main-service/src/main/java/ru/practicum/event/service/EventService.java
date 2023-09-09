package ru.practicum.event.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.event.dto.*;
import ru.practicum.user.dto.ParticipationRequestDto;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface EventService {
    EventFullDto getById(@NotNull Long id);

    List<EventShortDto> getAllByUser(@NotNull Long userId,
                                     @NotNull Integer from,
                                     @NotNull Integer size);

    EventFullDto create(@PathVariable Long userId,
                        @RequestBody NewEventDto dto);

    EventFullDto getById(@NotNull Long userId,
                         @NotNull Long eventId);

    EventFullDto updateByIdAndUserId(@NotNull Long userId,
                                     @NotNull Long eventId,
                                     @NotNull UpdateEventUserRequest request);

    ParticipationRequestDto getEventRequestsByUserId(@NotNull Long userId,
                                                     @NotNull Long eventId);

    EventRequestStatusUpdateResult changeEventStatusByUserId(@NotNull Long userId,
                                                             @NotNull Long eventId,
                                                             @NotNull EventRequestStatusUpdateRequest request);

    List<EventFullDto> search(Integer[] users,
                              String[] states,
                              Integer[] categories,
                              String rangeStart,
                              String rangeEnd,
                              @NotNull Integer from,
                              @NotNull Integer size);

    EventFullDto update(@NotNull Long eventId, @NotNull UpdateEventAdminRequest request);

    List<EventShortDto> getAll(String text,
                               Integer[] categories,
                               Boolean paid,
                               String rangeStart,
                               String rangeEnd,
                               Boolean onlyAvailable,
                               String sort,
                               @NotNull Integer from,
                               @NotNull Integer size);
}
