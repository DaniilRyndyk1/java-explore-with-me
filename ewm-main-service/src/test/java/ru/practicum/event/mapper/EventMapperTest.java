package ru.practicum.event.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.location.model.Location;
import ru.practicum.location.service.LocationService;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.Utils.dateTimeFormatter;
import static ru.practicum.event.TestData.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class EventMapperTest {
    private final EventMapper mapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final LocationService locationService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final EventService eventService;

    private NewEventDto newEventDto;
    private EventFullDto eventFullDto;
    private Event event;
    private Location location;
    private Category category;
    private User user;

    @BeforeEach
    public void setup() {
        location = locationService.create(locationDto);
        CategoryDto categoryDto = categoryService.create(newCategoryDto);
        category = categoryMapper.toCategory(categoryDto.getId(), categoryDto);
        UserDto userDto = userService.add(newUserRequest);
        user = userMapper.toUser(userDto.getId(), newUserRequest);
        newEventDto = new NewEventDto(
                annotation,
                category.getId(),
                description,
                eventDateRaw,
                locationDto,
                paid,
                participantLimit,
                requestModeration,
                title
        );
        eventFullDto = eventService.create(user.getId(), newEventDto);
        event = new Event(
                eventFullDto.getId(),
                annotation,
                category,
                0L,
                LocalDateTime.parse(eventFullDto.getCreatedOn(), dateTimeFormatter),
                description,
                eventDate,
                user,
                location,
                paid,
                0L,
                publishedOn,
                false,
                state,
                title,
                0L,
                null
        );
    }

    @Test
    void shouldConvertEventDtoToFullEventDto() {
        var result = mapper.toFullDto(event);
        assertNotNull(result);
        assertEquals(result.getId(), eventFullDto.getId());
        assertEquals(result.getAnnotation(), eventFullDto.getAnnotation());
        assertEquals(result.getConfirmedRequests(), 0L);
        assertEquals(result.getCategory().getId(), eventFullDto.getCategory().getId());
        assertEquals(result.getCreatedOn(), eventFullDto.getCreatedOn());
        assertEquals(result.getDescription(), eventFullDto.getDescription());
        assertEquals(result.getEventDate(), eventFullDto.getEventDate());
        assertEquals(result.getInitiator().getId(), eventFullDto.getInitiator().getId());
        assertEquals(result.getLocation().getLon(), eventFullDto.getLocation().getLon());
        assertEquals(result.getLocation().getLat(), eventFullDto.getLocation().getLat());
        assertEquals(result.getPaid(), eventFullDto.getPaid());
        assertEquals(result.getState(), eventFullDto.getState());
        assertEquals(result.getTitle(), eventFullDto.getTitle());
        assertEquals(result.getViews(), 0L);
    }

    @Test
    void shouldConvertNewEventToEvent() {
        var result = mapper.toEvent(
                newEventDto,
                category,
                user,
                location
        );
        assertNotNull(result);
        assertEquals(result.getAnnotation(), eventFullDto.getAnnotation());
        assertNull(result.getConfirmedRequests());
        assertEquals(result.getCategory().getId(), eventFullDto.getCategory().getId());
        assertNull(result.getCreatedOn());
        assertEquals(result.getDescription(), eventFullDto.getDescription());
        assertEquals(result.getEventDate().format(dateTimeFormatter), eventFullDto.getEventDate());
        assertEquals(result.getInitiator().getId(), eventFullDto.getInitiator().getId());
        assertEquals(result.getLocation().getLon(), eventFullDto.getLocation().getLon());
        assertEquals(result.getLocation().getLat(), eventFullDto.getLocation().getLat());
        assertEquals(result.getPaid(), eventFullDto.getPaid());
        assertNull(result.getState());
        assertEquals(result.getTitle(), eventFullDto.getTitle());
        assertNull(result.getViews());
    }

    @Test
    void shouldConvertEventToEventShortDto() {
        var result = mapper.toShortDto(event);
        assertNotNull(result);
        assertEquals(result.getId(), eventFullDto.getId());
        assertEquals(result.getAnnotation(), eventFullDto.getAnnotation());
        assertEquals(result.getConfirmedRequests(), 0L);
        assertEquals(result.getCategory().getId(), eventFullDto.getCategory().getId());
        assertEquals(result.getEventDate(), eventFullDto.getEventDate());
        assertEquals(result.getInitiator().getId(), eventFullDto.getInitiator().getId());
        assertEquals(result.getPaid(), eventFullDto.getPaid());
        assertEquals(result.getTitle(), eventFullDto.getTitle());
        assertEquals(result.getViews(), 0L);
    }
}
