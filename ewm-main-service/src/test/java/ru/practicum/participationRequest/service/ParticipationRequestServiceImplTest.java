package ru.practicum.participationRequest.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryServiceImpl;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.enums.EventStateAction;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.service.EventServiceImpl;
import ru.practicum.handler.ConflictException;
import ru.practicum.handler.NotFoundException;
import ru.practicum.location.service.LocationServiceImpl;
import ru.practicum.participationRequest.dto.EventRequestStatusUpdateRequest;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.enums.ParticipationRequestState;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.Utils.dateTimeFormatter;
import static ru.practicum.event.TestData.*;
import static ru.practicum.event.TestData.title;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class ParticipationRequestServiceImplTest {
    private final ParticipationRequestServiceImpl service;
    private final EventServiceImpl eventService;
    private final EventMapper eventMapper;
    private final UserServiceImpl userService;
    private final UserMapper userMapper;
    private final CategoryServiceImpl categoryService;
    private final LocationServiceImpl locationService;

    private User user;
    private User user2;
    private User user3;
    private CategoryDto category;
    private EventFullDto event;
    private ParticipationRequestDto request;
    private ParticipationRequestDto request2;

    @BeforeEach
    void setup() {
        var newUserRequest = new NewUserRequest(
                "Danila",
                "konosuba@yandex.ru"
        );

        var userDto = userService.add(newUserRequest);
        user = userMapper.toUser(userDto.getId(), newUserRequest);

        userDto = userService.add(new NewUserRequest("Nikita", "bebe@ya.ru"));
        user2 = userMapper.toUser(userDto.getId(), newUserRequest);

        userDto = userService.add(new NewUserRequest("Nikita2", "bebe2@ya.ru"));
        user3 = userMapper.toUser(userDto.getId(), newUserRequest);

        var categoryDto = new NewCategoryDto("The best");
        category = categoryService.create(categoryDto);

        var newEventDto = new NewEventDto(
                annotation,
                category.getId(),
                description,
                eventDateRaw,
                locationDto,
                paid,
                3L,
                true,
                title
        );

        event = eventService.create(user.getId(), newEventDto);

        eventService.update(event.getId(), new UpdateEventAdminRequest(
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

        request = service.create(user2.getId(), event.getId());
        request2 = service.create(user3.getId(), event.getId());
    }

    @Test
    void shouldGetById() {
        var result = service.getById(request.getId());
        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
        assertEquals(request.getEvent(), result.getEvent().getId());
        assertEquals(request.getRequester(), result.getRequester().getId());
        assertEquals(request.getCreated(), result.getCreated().format(dateTimeFormatter));

        result = service.getById(request2.getId());
        assertNotNull(result);
        assertEquals(request2.getId(), result.getId());
        assertEquals(request2.getEvent(), result.getEvent().getId());
        assertEquals(request2.getRequester(), result.getRequester().getId());
        assertEquals(request2.getCreated(), result.getCreated().format(dateTimeFormatter));
    }

    @Test
    void shouldGetByUserIdAndEventId() {
        var result = service.getById(user2.getId(), event.getId());
        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
        assertEquals(request.getEvent(), result.getEvent().getId());
        assertEquals(request.getRequester(), result.getRequester().getId());
        assertEquals(request.getCreated(), result.getCreated().format(dateTimeFormatter));

        result = service.getById(user3.getId(), event.getId());
        assertNotNull(result);
        assertEquals(request2.getId(), result.getId());
        assertEquals(request2.getEvent(), result.getEvent().getId());
        assertEquals(request2.getRequester(), result.getRequester().getId());
        assertEquals(request2.getCreated(), result.getCreated().format(dateTimeFormatter));
    }

    @Test
    void shouldGetDtoByUserIdAndEventId() {
        var result = service.getDtoById(user2.getId(), event.getId());
        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
        assertEquals(request.getEvent(), result.getEvent());
        assertEquals(request.getRequester(), result.getRequester());
        assertEquals(request.getCreated(), result.getCreated());
    }

    @Test
    void shouldNotGetDtoByIdsWithUserNotFoundId() {
        assertThrows(NotFoundException.class,
                () -> service.getDtoById(user.getId() - 1, event.getId()));
    }

    @Test
    void shouldNotGetDtoByIdsWithEventNotFoundId() {
        assertThrows(NotFoundException.class,
                () -> service.getDtoById(user.getId(), event.getId() - 1));
    }

    @Test
    void shouldNotGetByIdsWithUserNotFoundId() {
        assertThrows(NotFoundException.class,
                () -> service.getById(user.getId() - 1, event.getId()));
    }

    @Test
    void shouldNotGetByIdsWithEventNotFoundId() {
        assertThrows(NotFoundException.class,
                () -> service.getById(user.getId(), event.getId() - 1));
    }

    @Test
    void shouldNotGetByIdsWithRequestNotFoundId() {
        assertThrows(NotFoundException.class,
                () -> service.getById(user.getId(), event.getId()));
    }

    @Test
    void shouldNotGetByIdWithNotFoundId() {
        assertThrows(NotFoundException.class,
                () -> service.getById(request.getId() - 1));
    }

    @Test
    void shouldGetAll() {
        var result = service.getAll(user2.getId());
        assertNotNull(result);
        assertEquals(1, result.size());

        var tempRequest = result.get(0);

        assertEquals(request.getId(), tempRequest.getId());
        assertEquals(request.getEvent(), tempRequest.getEvent());
        assertEquals(request.getRequester(), tempRequest.getRequester());
        assertEquals(request.getCreated(), tempRequest.getCreated());
    }

    @Test
    void shouldGetAllWithWrongId() {
        assertThrows(NotFoundException.class,
                () -> service.getAll(user.getId() - 1));
    }

    @Test
    void shouldCreate() {
        var userDto = userService.add(new NewUserRequest("Nikita3", "333bebe@ya.ru"));
        var user3 = userMapper.toUser(userDto.getId(), newUserRequest);

        var result = service.create(user3.getId(), event.getId());
        assertNotNull(result);
        assertEquals(event.getId(), result.getEvent());
        assertEquals(user3.getId(), result.getRequester());
    }

    @Test
    void shouldSetStatusConfirmedWhenCreateAndRequestModerationIsFalse() {
        var newEventDto = new NewEventDto(
                annotation + "!",
                category.getId(),
                description + "!",
                eventDateRaw,
                locationDto,
                paid,
                participantLimit,
                false,
                title + "!"
        );

        var event2 = eventService.create(user.getId(), newEventDto);

        eventService.update(event2.getId(), new UpdateEventAdminRequest(
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

        var result = service.create(user2.getId(), event2.getId());
        assertNotNull(result);
        assertEquals(event2.getId(), result.getEvent());
        assertEquals(user2.getId(), result.getRequester());
        assertEquals(ParticipationRequestState.CONFIRMED, result.getStatus());
    }

    @Test
    void shouldNotCreateWithWrongUser() {
        assertThrows(NotFoundException.class,
                () -> service.create(user.getId() - 1, event.getId()));
    }

    @Test
    void shouldNotCreateWithWrongEvent() {
        var userDto = userService.add(new NewUserRequest("Nikita3", "333bebe@ya.ru"));
        var user3 = userMapper.toUser(userDto.getId(), newUserRequest);

        assertThrows(NotFoundException.class,
                () -> service.create(user3.getId(), event.getId() - 1));
    }

    @Test
    void shouldNotCreateWithInitiatorId() {
        assertThrows(ConflictException.class,
                () -> service.create(user.getId(), event.getId()));
    }

    @Test
    void shouldNotCreateRequestDuplicate() {
        assertThrows(ConflictException.class,
                () -> service.create(user2.getId(), event.getId()));
    }

    @Test
    void shouldNotCreateWithEventStatePENDING() {
        var newEventDto = new NewEventDto(
                annotation + "!",
                category.getId(),
                description + "!",
                eventDateRaw,
                locationDto,
                paid,
                participantLimit,
                true,
                title + "!"
        );

        var event2 = eventService.create(user.getId(), newEventDto);
        assertThrows(ConflictException.class,
                () -> service.create(user2.getId(), event2.getId()));
    }

    @Test
    void shouldNotCreateWithFullLimitEvent() {
        var userDto = userService.add(new NewUserRequest("Nikita3", "333bebe@ya.ru"));
        var user3 = userMapper.toUser(userDto.getId(), newUserRequest);

        var request3 = service.create(user3.getId(), event.getId());

        var updateRequest = new EventRequestStatusUpdateRequest(
                List.of(request.getId(), request2.getId(), request3.getId()),
                ParticipationRequestState.CONFIRMED
        );
        service.changeRequestsStatus(user.getId(), event.getId(), updateRequest);

        userDto = userService.add(new NewUserRequest("Nikita4", "3334bebe@ya.ru"));
        var user4 = userMapper.toUser(userDto.getId(), newUserRequest);

        assertThrows(ConflictException.class,
                () -> service.create(user4.getId(), event.getId()));
    }


    @Test
    void shouldCancel() {
        var result = service.cancel(user2.getId(), request.getId());
        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
        assertEquals(request.getEvent(), result.getEvent());
        assertEquals(request.getRequester(), result.getRequester());
        assertEquals(request.getCreated(), result.getCreated());
        assertEquals(ParticipationRequestState.CANCELED, result.getStatus());
    }


    @Test
    void shouldNotCancelWithWrongUserId() {
        assertThrows(NotFoundException.class,
                () -> service.cancel(user.getId() - 1, request.getId()));
    }

    @Test
    void shouldNotCancelWithUserIdNotRequester() {
        assertThrows(NotFoundException.class,
                () -> service.cancel(user3.getId(), request.getId()));
    }

    @Test
    void shouldUpdateRequestsStatuses() {
        var updateRequest = new EventRequestStatusUpdateRequest(
                List.of(request2.getId()),
                ParticipationRequestState.CANCELED
        );
        var result = service.changeRequestsStatus(user.getId(), event.getId(), updateRequest);
        assertNotNull(result);
        assertEquals(0, result.getConfirmedRequests().size());
        assertEquals(1, result.getRejectedRequests().size());
        assertEquals(request2.getId(), result.getRejectedRequests().get(0).getId());
    }

    @Test
    void shouldNotUpdateWithWrongEvent() {
        var updateRequest = new EventRequestStatusUpdateRequest(
                List.of(request2.getId()),
                ParticipationRequestState.CANCELED
        );
        assertThrows(NotFoundException.class,
                () -> service.changeRequestsStatus(user.getId(), event.getId() - 1, updateRequest));
    }

    @Test
    void shouldNotUpdateWithNotInitiatorUserId() {
        var updateRequest = new EventRequestStatusUpdateRequest(
                List.of(request2.getId()),
                ParticipationRequestState.CANCELED
        );
        assertThrows(NotFoundException.class,
                () -> service.changeRequestsStatus(user3.getId(), event.getId(), updateRequest));
    }
}