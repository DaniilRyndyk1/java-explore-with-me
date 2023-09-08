package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.event.enums.UpdateStatus;

import java.util.List;

@AllArgsConstructor
@Getter
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private UpdateStatus status;
}
