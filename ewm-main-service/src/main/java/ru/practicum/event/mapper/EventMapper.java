package ru.practicum.event.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.Utils;
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
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final DateTimeFormatter formatter = Utils.dateTimeFormatter;

    public EventFullDto toFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                categoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn().format(formatter),
                event.getDescription(),
                event.getEventDate().format(formatter),
                userMapper.toUserShortDto(event.getInitiator()),
                event.getLocation(),
                event.getPaid(),
                event.getPublishedOn() == null ? "" : event.getPublishedOn().format(formatter),
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
                LocalDateTime.parse(dto.getEventDate(), formatter),
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

//    public Event toEvent(EventFullDto dto, Category category, User user) {
//        return new Event(
//                dto.getId(),
//                dto.getAnnotation(),
//                category,
//                dto.getConfirmedRequests(),
//                LocalDateTime.parse(dto.getCreatedOn(), formatter),
//                dto.getDescription(),
//                LocalDateTime.parse(dto.getEventDate(), formatter),
//                user,
//                dto.getLocation(),
//                dto.getPaid(),
//                dto.getParticipantLimit(),
//                LocalDateTime.parse(dto.getPublishedOn(), formatter),
//                dto.getRequestModeration(),
//                dto.getState(),
//                dto.getTitle(),
//                dto.getViews(),
//                null
//        );
//    }

    public EventShortDto toShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                categoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate().format(formatter),
                userMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );
    }
}
