package ru.practicum.participationRequest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.service.EventService;
import ru.practicum.handler.ConflictException;
import ru.practicum.handler.NotFoundException;
import ru.practicum.participationRequest.dto.EventRequestStatusUpdateRequest;
import ru.practicum.participationRequest.dto.EventRequestStatusUpdateResult;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.enums.ParticipationRequestState;
import ru.practicum.participationRequest.mapper.ParticipationRequestMapper;
import ru.practicum.participationRequest.model.ParticipationRequest;
import ru.practicum.participationRequest.repository.ParticipationRequestRepository;
import ru.practicum.user.service.UserService;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository repository;
    private final ParticipationRequestMapper mapper;
    private final UserService userService;
    private final EventService eventService;

    @GetMapping("users/{userId}/requests")
    public List<ParticipationRequestDto> getAll(@PathVariable Long userId) {
        userService.getById(userId);

        return repository.findAllByRequester_Id(userId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("users/{userId}/requests")
    public ParticipationRequestDto create(@NotNull Long userId,
                                          @NotNull Long eventId) {
        var user = userService.getById(userId);
        var event = eventService.getById(eventId);
        if (repository.findFirstByRequester_IdAndEvent_Id(userId, eventId).isPresent()) {
            throw new ConflictException("could not execute statement; SQL [n/a]; constraint [uq_request]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement");
        }

        if (user.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException("DEBUG"); //TODO найти правильное сообщение
        }

        if (event.getState().equals(EventState.PENDING)) {
            throw new ConflictException("DEBUG"); //TODO найти правильное сообщение
        }

        //TODO сделать проверку лимита участников

        var state = ParticipationRequestState.WAITING;
        if (!event.getRequestModeration()) {
            state = ParticipationRequestState.CONFIRMED;
        }

        return mapper.toDto(
                repository.save(
                        new ParticipationRequest(
                                -1L,
                                LocalDateTime.now(),
                                event,
                                user,
                                state
                        )
                )
        );
    }

    public ParticipationRequestDto cancel(@PathVariable Long userId,
                                          @PathVariable Long requestId) {
        userService.getById(userId);
        repository.findById(requestId).orElseThrow(() -> new NotFoundException("DEBUG")); //TODO
        repository.setCanceledById(requestId);
        return mapper.toDto(
                repository.findById(requestId).get()
        );
    }

    public ParticipationRequestDto get(@NotNull Long userId,
                                       @NotNull Long eventId) {
        userService.getById(userId);
        eventService.getById(eventId);

        return mapper.toDto(
                repository.findFirstByRequester_IdAndEvent_Id(userId, eventId).orElseThrow(
                        () -> new NotFoundException("DEBUG") // TODO
                )
        );
    }

    public EventRequestStatusUpdateResult changeRequestsStatus(@NotNull Long userId,
                                                               @NotNull Long eventId,
                                                               @NotNull EventRequestStatusUpdateRequest request) {
        userService.getById(userId);
        eventService.getDtoById(eventId);

        repository.updateStatusesByIds(request.getRequestIds(), request.getStatus());
        var requests = repository.findAllConfirmedOrRejected();

        var confirmed = requests
                .stream()
                .filter(x -> x.getStatus().equals(ParticipationRequestState.CONFIRMED))
                .map(mapper::toDto)
                .collect(Collectors.toList());

        var rejected = requests
                .stream()
                .filter(x -> x.getStatus().equals(ParticipationRequestState.REJECTED))
                .map(mapper::toDto)
                .collect(Collectors.toList());


        return new EventRequestStatusUpdateResult(confirmed, rejected);
    }
}
