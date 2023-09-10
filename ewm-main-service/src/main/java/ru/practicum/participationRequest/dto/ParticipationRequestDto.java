package ru.practicum.participationRequest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.event.enums.EventState;
import ru.practicum.participationRequest.enums.ParticipationRequestState;

import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
public class ParticipationRequestDto {
    private Long id;

    @Size(min = 23, max = 23)
    private String created;
    private Long event;
    private Long requester;
    private ParticipationRequestState status;
}
