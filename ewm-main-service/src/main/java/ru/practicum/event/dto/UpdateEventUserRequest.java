package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.event.enums.EventStateAction;
import ru.practicum.event.model.Location;

import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    @Size(min = 19, max = 19)
    private String eventDate;

    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventStateAction stateAction;

    @Size(min = 3, max = 120)
    private String title;
}
