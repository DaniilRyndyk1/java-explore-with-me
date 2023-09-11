package ru.practicum.participationRequest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    public ParticipationRequest getById(@NotNull Long id) {
        var message = "Participation Request with id=" + id + " was not found";

        return repository.findById(id).orElseThrow(
                () -> new NotFoundException(message)
        );
    }

    public ParticipationRequest getById(@NotNull Long userId,
                                        @NotNull Long eventId) {
        userService.getById(userId);
        eventService.getById(eventId);

        var message = "Participation Request with userId=" + userId + " and eventId=" + eventId + " was not found";

        return repository.findFirstByRequester_IdAndEvent_Id(userId, eventId).orElseThrow(
                () -> new NotFoundException(message)
        );
    }

    public ParticipationRequestDto getDtoById(@NotNull Long userId,
                                              @NotNull Long eventId) {
        return mapper.toDto(
                getById(userId, eventId)
        );
    }

    public List<ParticipationRequestDto> getAll(@NotNull Long userId) {
        userService.getById(userId);

        return repository.findAllByRequester_Id(userId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

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

        var hasParticipantLimit = event.getParticipantLimit() != 0L;

        if (hasParticipantLimit && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("DEBUG"); //TODO найти правильное сообщение
        }

        var request = repository.save(
                new ParticipationRequest(
                        -1L,
                        LocalDateTime.now(),
                        event,
                        user,
                        ParticipationRequestState.WAITING
                )
        );

        if (!event.getRequestModeration()) {
            request.setStatus(ParticipationRequestState.CONFIRMED);
            repository.incrementConfirmedRequestsById(request.getId());
        }

        return mapper.toDto(
                repository.save(request)
        );
    }

    public ParticipationRequestDto cancel(@NotNull Long userId,
                                          @NotNull Long requestId) {
        userService.getById(userId);
        getById(requestId);

        repository.setCanceledById(requestId);
        repository.decrementConfirmedRequestsById(requestId);
        return mapper.toDto(getById(requestId));
    }

    public EventRequestStatusUpdateResult changeRequestsStatus(@NotNull Long userId,
                                                               @NotNull Long eventId,
                                                               @NotNull EventRequestStatusUpdateRequest request) {
        repository.updateStatusesByIds(
                request.getRequestIds(),
                request.getStatus(),
                userId
        );

        var requests = repository.findAllConfirmedOrRejected(userId, eventId);

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
