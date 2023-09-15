package ru.practicum.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.event.enums.EventStateAction;
import ru.practicum.location.dto.LocationDto;

@NoArgsConstructor
@Getter
public class UpdateEventAdminRequest extends UpdateEventUserRequest {
    public UpdateEventAdminRequest(String annotation, Long category, String description, String eventDate, LocationDto location, Boolean paid, Long participantLimit, Boolean requestModeration, EventStateAction stateAction, String title) {
        super(annotation,
                category,
                description,
                eventDate,
                location,
                paid,
                participantLimit,
                requestModeration,
                stateAction,
                title);
    }
}