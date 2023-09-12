package ru.practicum.participationRequest.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.model.ParticipationRequest;

import static ru.practicum.Utils.dateTimeFormatter;

@Component
public class ParticipationRequestMapper {
    public ParticipationRequestDto toDto(ParticipationRequest request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated().format(dateTimeFormatter),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus()
        );
    }
}
