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
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Request with id=" + id + " was not found")
        );
    }

    public ParticipationRequest getById(@NotNull Long userId,
                                        @NotNull Long eventId) {
        userService.getById(userId);
        eventService.getById(eventId);

        return repository.findFirstByRequester_IdAndEvent_Id(userId, eventId).orElseThrow(
                () -> new NotFoundException("Request with userId=" + userId + " and eventId=" + eventId + " was not found")
        );
    }

    public ParticipationRequestDto getDtoById(@NotNull Long userId,
                                              @NotNull Long eventId) {
        return mapper.toDto(getById(userId, eventId));
    }

    public List<ParticipationRequestDto> getAllByInitiatorIdAndEventId(@NotNull Long userId, @NotNull Long eventId) {
        userService.getById(userId);
        var event = eventService.getById(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        return repository.findAllByEvent_Id(eventId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
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
            throw new ConflictException("User is initiator");
        }

        if (event.getState().equals(EventState.PENDING)) {
            throw new ConflictException("Event in PENDING state");
        }

        var hasParticipantLimit = event.getParticipantLimit() != 0L;

        if (hasParticipantLimit && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Limit is full");
        }

        var request = repository.save(
                new ParticipationRequest(
                        -1L,
                        LocalDateTime.now(),
                        event,
                        user,
                        ParticipationRequestState.PENDING
                )
        );

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(ParticipationRequestState.CONFIRMED);
            repository.incrementConfirmedRequestsByEventId(event.getId(), 1L);
        }

        return mapper.toDto(repository.save(request));
    }

    public ParticipationRequestDto cancel(@NotNull Long userId,
                                          @NotNull Long requestId) {
        userService.getById(userId);
        var request = getById(requestId);

        if (!request.getRequester().getId().equals(userId)) {
            throw new NotFoundException("Request with id=" + requestId + " was not found");
        }

        repository.setCanceledById(requestId);
        repository.decrementConfirmedRequestsByEventId(request.getEvent().getId(), 1L);
        return mapper.toDto(getById(requestId));
    }

    public EventRequestStatusUpdateResult changeRequestsStatus(@NotNull Long userId,
                                                               @NotNull Long eventId,
                                                               @NotNull EventRequestStatusUpdateRequest request) {

        var event = eventService.getById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("User isn't initiator");
        }

        var ids = request.getRequestIds();
        var status = request.getStatus();

        if (ids.size() > event.getParticipantLimit() - event.getConfirmedRequests()) {
            throw new ConflictException("Limit is reached");
        }

        if (repository.countCanceledRequestsByIds(ids) > 0) {
            throw new ConflictException("One of requests were canceled");
        }

        repository.updateStatusesByIds(ids, status);

        var size = (long) ids.size();

        if (status.equals(ParticipationRequestState.CANCELED)) {
            repository.decrementConfirmedRequestsByEventId(eventId, size);
        } else {
            repository.incrementConfirmedRequestsByEventId(eventId, size);
        }

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
