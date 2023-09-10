package ru.practicum.participationRequest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.participationRequest.enums.ParticipationRequestState;

import java.util.List;

@AllArgsConstructor
@Getter
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private ParticipationRequestState status;
}
