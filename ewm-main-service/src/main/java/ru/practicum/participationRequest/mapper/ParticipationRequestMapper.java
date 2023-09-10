package ru.practicum.participationRequest.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.Utils;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.model.ParticipationRequest;

@Component
public class ParticipationRequestMapper {
    public ParticipationRequestDto toDto(ParticipationRequest request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated().format(Utils.dateTimeFormatter),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus()
        );
    }
}
