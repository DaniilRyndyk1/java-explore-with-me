package ru.practicum.participationRequest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import ru.practicum.participationRequest.enums.ParticipationRequestState;

@AllArgsConstructor
@Getter
public class ParticipationRequestDto {
    private Long id;
    private String created;
    private Long event;
    private Long requester;
    private ParticipationRequestState status;
}
