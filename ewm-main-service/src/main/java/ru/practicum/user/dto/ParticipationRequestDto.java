package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.event.enums.EventState;

import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
public class ParticipationRequestDto {
    @Size(min = 23, max = 23)
    private String created;

    private Long event;
    private Long id;
    private Long requester;
    private EventState status;
}
