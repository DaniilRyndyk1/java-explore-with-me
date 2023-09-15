package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryServiceImpl;
import ru.practicum.event.dto.*;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.enums.EventStateAction;
import ru.practicum.handler.ConflictException;
import ru.practicum.handler.EntityNotFoundException;
import ru.practicum.statservice.StatClient;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.Utils.dateTimeFormatter;
import static ru.practicum.event.TestData.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class EventServiceImplTest {
    private final EventServiceImpl service;
    private final UserServiceImpl userService;
    private final UserMapper userMapper;
    private final CategoryServiceImpl categoryService;

    @MockBean
    private StatClient client;

    private User user;
    private User user2;
    private EventFullDto event;
    private EventFullDto event2;

    @BeforeEach
    void setup() {
        var userDto = userService.add(new NewUserRequest("Danila", "konosuba@ya.ru"));
        user = userMapper.toUser(userDto.getId(), newUserRequest);

        userDto = userService.add(new NewUserRequest("Nikita", "bebe@ya.ru"));
        user2 = userMapper.toUser(userDto.getId(), newUserRequest);

        userDto = userService.add(new NewUserRequest("Nikita2", "bebe2@ya.ru"));
        userMapper.toUser(userDto.getId(), newUserRequest);

        var categoryDto = new NewCategoryDto("The best");
        CategoryDto category = categoryService.create(categoryDto);

        event = service.create(user.getId(),
                new NewEventDto(
                        annotation,
                        category.getId(),
                        description,
                        eventDateRaw,
                        locationDto,
                        paid,
                        3L,
                        true,
                        title
                ));

        event = service.update(event.getId(), new UpdateEventAdminRequest(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                EventStateAction.PUBLISH_EVENT,
                null
        ));

        event2 = service.create(user.getId(),
                new NewEventDto(
                        annotation + "!",
                        category.getId(),
                        description + "!",
                        eventDateRaw,
                        locationDto,
                        paid,
                        3L,
                        true,
                        title + "!"
                ));
    }

    @Test
    void getDtoById() {
        var result = service.getDtoById(event.getId());
        assertNotNull(result);
        assertEquals(event.getId(), result.getId());
        assertEquals(event.getTitle(), result.getTitle());
        assertEquals(event.getAnnotation(), result.getAnnotation());
        assertEquals(event.getEventDate(), result.getEventDate());
        assertEquals(event.getPaid(), result.getPaid());
        assertEquals(event.getCategory().getId(), result.getCategory().getId());
        assertEquals(event.getInitiator().getId(), result.getInitiator().getId());
        assertEquals(event.getLocation().getLat(), result.getLocation().getLat());
        assertEquals(event.getLocation().getLon(), result.getLocation().getLon());
        assertEquals(event.getState(), result.getState());
        assertEquals(event.getViews(), result.getViews());
        assertEquals(event.getConfirmedRequests(), result.getConfirmedRequests());
        assertEquals(event.getCreatedOn(), result.getCreatedOn());
        assertEquals(event.getParticipantLimit(), result.getParticipantLimit());
        assertEquals(event.getRequestModeration(), result.getRequestModeration());
    }

    @Test
    void shouldNotGetDtoByIdWithNotFoundId() {
        assertThrows(EntityNotFoundException.class,
                () -> service.getDtoById(event.getId() - 1));
    }

    @Test
    void shouldNotGetNotPublishedDtoById() {
        assertThrows(EntityNotFoundException.class,
                () -> service.getDtoById(event2.getId()));
    }

    @Test
    void shouldGetById() {
        var result = service.getById(event.getId());
        assertNotNull(result);
        assertEquals(event.getId(), result.getId());
        assertEquals(event.getTitle(), result.getTitle());
        assertEquals(event.getAnnotation(), result.getAnnotation());
        assertEquals(event.getEventDate(), result.getEventDate().format(dateTimeFormatter));
        assertEquals(event.getPaid(), result.getPaid());
        assertEquals(event.getCategory().getId(), result.getCategory().getId());
        assertEquals(event.getInitiator().getId(), result.getInitiator().getId());
        assertEquals(event.getLocation().getLat(), result.getLocation().getLat());
        assertEquals(event.getLocation().getLon(), result.getLocation().getLon());
        assertEquals(event.getState(), result.getState());
        assertEquals(event.getViews(), result.getViews());
        assertEquals(event.getConfirmedRequests(), result.getConfirmedRequests());
        assertEquals(event.getCreatedOn(), result.getCreatedOn().format(dateTimeFormatter));
        assertEquals(event.getParticipantLimit().longValue(), result.getParticipantLimit());
        assertEquals(event.getRequestModeration(), result.getRequestModeration());
    }

    @Test
    void shouldNotGetByIdWithNotFoundId() {
        assertThrows(EntityNotFoundException.class,
                () -> service.getDtoById(event.getId() - 1));
    }

    @Test
    void shouldGetByIds() {
        var result = service.getDtoById(user.getId(), event.getId());
        assertNotNull(result);
        assertEquals(event.getId(), result.getId());
        assertEquals(event.getTitle(), result.getTitle());
        assertEquals(event.getAnnotation(), result.getAnnotation());
        assertEquals(event.getEventDate(), result.getEventDate());
        assertEquals(event.getPaid(), result.getPaid());
        assertEquals(event.getCategory().getId(), result.getCategory().getId());
        assertEquals(event.getInitiator().getId(), result.getInitiator().getId());
        assertEquals(event.getLocation().getLat(), result.getLocation().getLat());
        assertEquals(event.getLocation().getLon(), result.getLocation().getLon());
        assertEquals(event.getState(), result.getState());
        assertEquals(event.getViews(), result.getViews());
        assertEquals(event.getConfirmedRequests(), result.getConfirmedRequests());
        assertEquals(event.getCreatedOn(), result.getCreatedOn());
        assertEquals(event.getParticipantLimit().longValue(), result.getParticipantLimit());
        assertEquals(event.getRequestModeration(), result.getRequestModeration());
    }

    @Test
    void shouldNotGetByIdsWithNotFoundUserId() {
        assertThrows(EntityNotFoundException.class,
                () -> service.getDtoById(user.getId() - 1, event.getId()));
    }

    @Test
    void shouldNotGetByIdsWithNotFoundEventId() {
        assertThrows(EntityNotFoundException.class,
                () -> service.getDtoById(user.getId(), event.getId() - 1));
    }

    @Test
    void shouldNotGetByIdsWithNotInitiatorUserId() {
        assertThrows(EntityNotFoundException.class,
                () -> service.getDtoById(user2.getId(), event.getId()));
    }

    @Test
    void shouldGetAllByUser() {
        var result = service.getAllByUser(user.getId(), 0, 10);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(event.getId(), result.get(0).getId());
        assertEquals(event2.getId(), result.get(1).getId());
    }

    @Test
    void create() {

    }

    @Test
    void shouldNotAdminUpdateWhenStateNotPendingAndWePublishEvent() {
        assertThrows(ConflictException.class,
                () -> service.update(event.getId(), new UpdateEventAdminRequest(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        EventStateAction.PUBLISH_EVENT,
                        null
                ))
        );
    }

    @Test
    void shouldNotAdminUpdateWhenStatePublishedAndWeCancelReview() {
        assertThrows(UnsupportedOperationException.class,
                () -> service.update(event.getId(), new UpdateEventAdminRequest(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        EventStateAction.CANCEL_REVIEW,
                        null
                ))
        );
    }

    @Test
    void shouldUserUpdate() {
        var result = service.update(user.getId(), event2.getId(), new UpdateEventUserRequest(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                EventStateAction.PUBLISH_EVENT,
                null
        ));
        assertNotNull(result);
        assertEquals(event2.getId(), result.getId());
        assertEquals(event2.getTitle(), result.getTitle());
        assertEquals(event2.getAnnotation(), result.getAnnotation());
        assertEquals(event2.getEventDate(), result.getEventDate());
        assertEquals(event2.getPaid(), result.getPaid());
        assertEquals(event2.getCategory().getId(), result.getCategory().getId());
        assertEquals(event2.getInitiator().getId(), result.getInitiator().getId());
        assertEquals(event2.getLocation().getLat(), result.getLocation().getLat());
        assertEquals(event2.getLocation().getLon(), result.getLocation().getLon());
        assertEquals(EventState.PUBLISHED, result.getState());
        assertEquals(event2.getViews(), result.getViews());
        assertEquals(event2.getConfirmedRequests(), result.getConfirmedRequests());
        assertEquals(event2.getCreatedOn(), result.getCreatedOn());
        assertEquals(event2.getParticipantLimit().longValue(), result.getParticipantLimit());
        assertEquals(event2.getRequestModeration(), result.getRequestModeration());
    }

    @Test
    void shouldNotAdminUpdateWhenStateIsPublished() {
        assertThrows(ConflictException.class,
                () -> service.update(user.getId(), event.getId(), new UpdateEventUserRequest(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        EventStateAction.PUBLISH_EVENT,
                        null
                ))
        );
    }

    @Test
    void shouldSearch() {
        var list = service.search(
                List.of(user.getId()),
                null,
                null,
                LocalDateTime.now().minusDays(100).format(dateTimeFormatter),
                LocalDateTime.now().plusDays(100).format(dateTimeFormatter),
                0,
                15
        );
        assertNotNull(list);
        assertEquals(2, list.size());

        var result = list.get(0);

        assertEquals(event.getId(), result.getId());
        assertEquals(event.getTitle(), result.getTitle());
        assertEquals(event.getAnnotation(), result.getAnnotation());
        assertEquals(event.getEventDate(), result.getEventDate());
        assertEquals(event.getPaid(), result.getPaid());
        assertEquals(event.getCategory().getId(), result.getCategory().getId());
        assertEquals(event.getInitiator().getId(), result.getInitiator().getId());
        assertEquals(event.getLocation().getLat(), result.getLocation().getLat());
        assertEquals(event.getLocation().getLon(), result.getLocation().getLon());
        assertEquals(event.getState(), result.getState());
        assertEquals(event.getViews(), result.getViews());
        assertEquals(event.getConfirmedRequests(), result.getConfirmedRequests());
        assertEquals(event.getCreatedOn(), result.getCreatedOn());
        assertEquals(event.getParticipantLimit().longValue(), result.getParticipantLimit());
        assertEquals(event.getRequestModeration(), result.getRequestModeration());
    }

    @Test
    void shouldSearchWithoutRange() {
        var list = service.search(
                List.of(user.getId()),
                null,
                null,
                null,
                null,
                0,
                15
        );
        assertNotNull(list);
        assertEquals(2, list.size());

        var result = list.get(0);

        assertEquals(event.getId(), result.getId());
        assertEquals(event.getTitle(), result.getTitle());
        assertEquals(event.getAnnotation(), result.getAnnotation());
        assertEquals(event.getEventDate(), result.getEventDate());
        assertEquals(event.getPaid(), result.getPaid());
        assertEquals(event.getCategory().getId(), result.getCategory().getId());
        assertEquals(event.getInitiator().getId(), result.getInitiator().getId());
        assertEquals(event.getLocation().getLat(), result.getLocation().getLat());
        assertEquals(event.getLocation().getLon(), result.getLocation().getLon());
        assertEquals(event.getState(), result.getState());
        assertEquals(event.getViews(), result.getViews());
        assertEquals(event.getConfirmedRequests(), result.getConfirmedRequests());
        assertEquals(event.getCreatedOn(), result.getCreatedOn());
        assertEquals(event.getParticipantLimit().longValue(), result.getParticipantLimit());
        assertEquals(event.getRequestModeration(), result.getRequestModeration());
    }

    @Test
    void shouldGetAll() {
        var list = service.getAll(
                null,
                null,
                null,
                null,
                null,
                false,
                "EVENT_DATE",
                0,
                15,
                "0.0.0.0"
        );

        assertNotNull(list);
        assertEquals(1, list.size());

        var result = list.get(0);

        assertEquals(event.getId(), result.getId());
        assertEquals(event.getTitle(), result.getTitle());
        assertEquals(event.getAnnotation(), result.getAnnotation());
        assertEquals(event.getEventDate(), result.getEventDate());
        assertEquals(event.getPaid(), result.getPaid());
        assertEquals(event.getCategory().getId(), result.getCategory().getId());
        assertEquals(event.getInitiator().getId(), result.getInitiator().getId());
        assertEquals(event.getViews(), result.getViews());
        assertEquals(event.getConfirmedRequests(), result.getConfirmedRequests());
    }

    @Test
    void shouldGetAllWithOnlyAvailable() {
        var list = service.getAll(
                null,
                null,
                null,
                null,
                null,
                true,
                "EVENT_DATE",
                0,
                15,
                "0.0.0.0"
        );

        assertNotNull(list);
        assertEquals(1, list.size());

        var result = list.get(0);

        assertEquals(event.getId(), result.getId());
        assertEquals(event.getTitle(), result.getTitle());
        assertEquals(event.getAnnotation(), result.getAnnotation());
        assertEquals(event.getEventDate(), result.getEventDate());
        assertEquals(event.getPaid(), result.getPaid());
        assertEquals(event.getCategory().getId(), result.getCategory().getId());
        assertEquals(event.getInitiator().getId(), result.getInitiator().getId());
        assertEquals(event.getViews(), result.getViews());
        assertEquals(event.getConfirmedRequests(), result.getConfirmedRequests());
    }

    @Test
    void shouldGetAllWithRange() {
        var list = service.getAll(
                null,
                null,
                null,
                LocalDateTime.now().minusDays(100).format(dateTimeFormatter),
                LocalDateTime.now().plusDays(100).format(dateTimeFormatter),
                false,
                "VIEWS",
                0,
                15,
                "0.0.0.0"
        );

        assertNotNull(list);
        assertEquals(1, list.size());

        var result = list.get(0);

        assertEquals(event.getId(), result.getId());
        assertEquals(event.getTitle(), result.getTitle());
        assertEquals(event.getAnnotation(), result.getAnnotation());
        assertEquals(event.getEventDate(), result.getEventDate());
        assertEquals(event.getPaid(), result.getPaid());
        assertEquals(event.getCategory().getId(), result.getCategory().getId());
        assertEquals(event.getInitiator().getId(), result.getInitiator().getId());
        assertEquals(event.getViews(), result.getViews());
        assertEquals(event.getConfirmedRequests(), result.getConfirmedRequests());
    }

    @Test
    void shouldGetWithUnsupportedSortType() {
        assertThrows(UnsupportedOperationException.class,
                () -> service.getAll(
                        null,
                        null,
                        null,
                        LocalDateTime.now().minusDays(100).format(dateTimeFormatter),
                        LocalDateTime.now().plusDays(100).format(dateTimeFormatter),
                        false,
                        "ERRRRRROR",
                        0,
                        15,
                        "0.0.0.0"
                )
        );
    }
}