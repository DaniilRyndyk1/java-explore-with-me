package ru.practicum.event.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.event.dto.*;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.model.Event;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

public interface EventService {
    EventFullDto getDtoById(@NotNull Long id);

    Event getById(@NotNull Long id);
    void checkExistsById(@NotNull Long id);

    List<EventShortDto> getAllByUser(@NotNull Long userId,
                                     @NotNull Integer from,
                                     @NotNull Integer size);

    EventFullDto create(@PathVariable Long userId,
                        @RequestBody NewEventDto dto);

    EventFullDto getDtoById(@NotNull Long userId,
                            @NotNull Long eventId);

    EventFullDto update(@NotNull Long userId,
                        @NotNull Long eventId,
                        @NotNull UpdateEventUserRequest request);

    List<EventFullDto> search(Long[] users,
                              EventState[] states,
                              Long[] categories,
                              String rangeStart,
                              String rangeEnd,
                              @NotNull Integer from,
                              @NotNull Integer size);

    EventFullDto update(@NotNull Long eventId, @NotNull UpdateEventAdminRequest request);

    List<EventShortDto> getAll(String text,
                               Long[] categories,
                               Boolean paid,
                               String rangeStart,
                               String rangeEnd,
                               Boolean onlyAvailable,
                               String sort,
                               @NotNull Integer from,
                               @NotNull Integer size,
                               @NotNull String ip);

    Set<Event> getAllByIds(Set<Long> ids);
}
