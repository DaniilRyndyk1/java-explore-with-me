package ru.practicum.event.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.location.model.Location;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

import static ru.practicum.Utils.dateTimeFormatter;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public EventFullDto toFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                categoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn().format(dateTimeFormatter),
                event.getDescription(),
                event.getEventDate().format(dateTimeFormatter),
                userMapper.toUserShortDto(event.getInitiator()),
                event.getLocation(),
                event.getPaid(),
                event.getPublishedOn() == null ? "" : event.getPublishedOn().format(dateTimeFormatter),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

    public Event toEvent(NewEventDto dto, Category category, User initiator, Location location) {
        var event = new Event(
                dto.getAnnotation(),
                category,
                dto.getDescription(),
                LocalDateTime.parse(dto.getEventDate(), dateTimeFormatter),
                initiator,
                location,
                dto.getPaid(),
                dto.getTitle()
        );
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.getRequestModeration());
        event.setCreatedOn(LocalDateTime.now());
        event.setConfirmedRequests(0L);
        return event;
    }

    public EventShortDto toShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                categoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate().format(dateTimeFormatter),
                userMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );
    }
}
