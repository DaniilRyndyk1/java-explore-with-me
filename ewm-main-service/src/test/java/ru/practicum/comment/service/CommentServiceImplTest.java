package ru.practicum.comment.service;

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
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentAdminRequest;
import ru.practicum.comment.dto.UpdateCommentUserRequest;
import ru.practicum.comment.enums.CommentStatus;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.enums.EventStateAction;
import ru.practicum.event.service.EventServiceImpl;
import ru.practicum.handler.ConflictException;
import ru.practicum.handler.EntityNotFoundException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserServiceImpl;

import static ru.practicum.event.TestData.*;
import static ru.practicum.event.TestData.title;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class CommentServiceImplTest {
    private final CommentServiceImpl service;
    private final EventServiceImpl eventService;
    private final UserServiceImpl userService;
    private final UserMapper userMapper;
    private final CategoryServiceImpl categoryService;

    private User user;
    private User user2;
    private EventFullDto event;
    private EventFullDto event2;
    private CommentDto comment;
    private NewCommentDto newCommentDtoForUser2;
    private UpdateCommentUserRequest updateCommentUserRequest;

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

        event = eventService.create(user.getId(),
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

        event = eventService.update(event.getId(), new UpdateEventAdminRequest(
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

        event2 = eventService.create(user.getId(),
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

        comment = service.create(user.getId(), event.getId(), new NewCommentDto("Swag", 4));
        newCommentDtoForUser2 = new NewCommentDto("SUPERRRRR", 5);
        updateCommentUserRequest = new UpdateCommentUserRequest("LOOONG WAITING", 2);
    }

    @Test
    void shouldGetDtoByIds() {
        var result = service.getDtoByIds(user.getId(), event.getId());
        assertNotNull(result);
        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getEvent().getId(), result.getEvent().getId());
        assertEquals(comment.getUser().getId(), result.getUser().getId());
        assertEquals(comment.getText(), result.getText());
        assertEquals(comment.getRating(), result.getRating());
        assertEquals(comment.getStatus(), result.getStatus());
    }

    @Test
    void shouldNotGetDtoByIdsWithNotFoundUserId() {
        assertThrows(EntityNotFoundException.class,
                () -> service.getDtoByIds(user.getId() - 1, event.getId()));
    }

    @Test
    void shouldNotGetDtoByIdsWithNotFoundEventId() {
        assertThrows(EntityNotFoundException.class,
                () -> service.getDtoByIds(user.getId(), event.getId() - 1));
    }

    @Test
    void shouldGetById() {
        var result = service.getById(comment.getId());
        assertNotNull(result);
        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getEvent().getId(), result.getEvent().getId());
        assertEquals(comment.getUser().getId(), result.getUser().getId());
        assertEquals(comment.getText(), result.getText());
        assertEquals(comment.getRating(), result.getRating());
        assertEquals(comment.getStatus(), result.getStatus());
    }

    @Test
    void shouldNotGetByIdWithNotFoundId() {
        assertThrows(EntityNotFoundException.class,
                () -> service.getById(comment.getId() - 1));
    }

    @Test
    void shouldNotGetByIdWithRejectedStatus() {
        service.updateByAdmin(user.getId(), event.getId(), new UpdateCommentAdminRequest(
                comment.getText(),
                comment.getRating(),
                CommentStatus.REJECTED
        ));

        assertThrows(EntityNotFoundException.class,
                () -> service.getById(comment.getId()));
    }

    @Test
    void shouldGetByIds() {
        var result = service.getByIds(user.getId(), event.getId());
        assertNotNull(result);
        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getEvent().getId(), result.getEvent().getId());
        assertEquals(comment.getUser().getId(), result.getUser().getId());
        assertEquals(comment.getText(), result.getText());
        assertEquals(comment.getRating(), result.getRating());
        assertEquals(comment.getStatus(), result.getStatus());
    }

    @Test
    void shouldNotGetByIdsWithNotFoundUserId() {
        assertThrows(EntityNotFoundException.class,
                () -> service.getByIds(user.getId() - 1, event.getId()));
    }

    @Test
    void shouldNotGetByIdsWithRejectedStatus() {
        service.updateByAdmin(user.getId(), event.getId(), new UpdateCommentAdminRequest(
                comment.getText(),
                comment.getRating(),
                CommentStatus.REJECTED
        ));

        assertThrows(EntityNotFoundException.class,
                () -> service.getByIds(user.getId(), event.getId()));
    }

    @Test
    void shouldNotGetByIdsWithNotFoundEventId() {
        assertThrows(EntityNotFoundException.class,
                () -> service.getByIds(user.getId(), event.getId() - 1));
    }

    @Test
    void shouldCheckExistsByIds() {
        assertDoesNotThrow(
                () -> service.checkExistsByIds(user.getId(), event.getId()));
    }

    @Test
    void shouldNotCheckExistsByIdsWithNotFoundUser() {
        assertThrows(EntityNotFoundException.class,
                () -> service.checkExistsByIds(user.getId() - 1, event.getId()));
    }

    @Test
    void shouldNotCheckExistsByIdsWithNotFoundEvent() {
        assertThrows(EntityNotFoundException.class,
                () -> service.checkExistsByIds(user.getId(), event.getId() - 1));
    }

    @Test
    void shouldCreate() {
        var result = service.create(user2.getId(), event.getId(), newCommentDtoForUser2);
        assertNotNull(result);
        assertEquals(comment.getId() + 1, result.getId());
        assertEquals(event.getId(), result.getEvent().getId());
        assertEquals(user2.getId(), result.getUser().getId());
        assertEquals(newCommentDtoForUser2.getText(), result.getText());
        assertEquals(newCommentDtoForUser2.getRating(), result.getRating());
        assertEquals(CommentStatus.PENDING, result.getStatus());
    }

    @Test
    void shouldNotDuplicateCreate() {
        assertThrows(ConflictException.class,
                () -> service.create(user.getId(), event.getId(), newCommentDtoForUser2));
    }

    @Test
    void shouldNotCreateWithNotFoundUser() {
        assertThrows(EntityNotFoundException.class,
                () -> service.create(user2.getId() - 999L, event.getId(), newCommentDtoForUser2));
    }

    @Test
    void shouldNotCreateWithNotFoundEvent() {
        assertThrows(EntityNotFoundException.class,
                () -> service.create(user2.getId(), event.getId() - 999L, newCommentDtoForUser2));
    }

    @Test
    void shouldDeleteByIds() {
        var userId = user.getId();
        var eventId = event.getId();
        service.delete(userId, eventId);
        assertThrows(EntityNotFoundException.class,
                () -> service.getByIds(userId, eventId));
    }

    @Test
    void shouldNotDeleteByIdsWithConfirmedStatus() {
        service.updateByAdmin(user.getId(), event.getId(), new UpdateCommentAdminRequest(
                comment.getText(),
                comment.getRating(),
                CommentStatus.CONFIRMED
        ));

        assertThrows(ConflictException.class,
                () -> service.delete(user.getId(), event.getId()));
    }

    @Test
    void shouldNotDeleteByIdsWithNotFoundUserId() {
        assertThrows(EntityNotFoundException.class,
                () -> service.delete(user.getId() - 1, event.getId()));
    }

    @Test
    void shouldNotDeleteByIdsWithNotFoundEventId() {
        assertThrows(EntityNotFoundException.class,
                () -> service.delete(user.getId(), event.getId() - 1));
    }

    @Test
    void shouldUpdate() {
        var result = service.update(user.getId(), event.getId(), updateCommentUserRequest);
        assertNotNull(result);
        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getEvent().getId(), result.getEvent().getId());
        assertEquals(comment.getUser().getId(), result.getUser().getId());
        assertEquals(updateCommentUserRequest.getText(), result.getText());
        assertEquals(updateCommentUserRequest.getRating(), result.getRating());
        assertEquals(comment.getStatus(), result.getStatus());
    }

    @Test
    void shouldNotUpdateByIdsWithNotFoundUserId() {
        assertThrows(EntityNotFoundException.class,
                () -> service.update(user.getId() - 1, event.getId(), updateCommentUserRequest));
    }

    @Test
    void shouldNotUpdateByIdsWithNotFoundEventId() {
        assertThrows(EntityNotFoundException.class,
                () -> service.update(user.getId(), event.getId() - 1, updateCommentUserRequest));
    }

    @Test
    void shouldGetAllByEventId() {
        var comment2 = service.create(user2.getId(), event.getId(), newCommentDtoForUser2);

        var result = service.getAllByEvent(event.getId(), 0, 15);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(comment.getId(), result.get(0).getId());
        assertEquals(comment.getEvent().getId(), result.get(0).getEvent().getId());
        assertEquals(comment.getUser().getId(), result.get(0).getUser().getId());
        assertEquals(comment.getText(), result.get(0).getText());
        assertEquals(comment.getRating(), result.get(0).getRating());
        assertEquals(comment.getStatus(), result.get(0).getStatus());

        assertEquals(comment2.getId(), result.get(1).getId());
        assertEquals(comment2.getEvent().getId(), result.get(1).getEvent().getId());
        assertEquals(comment2.getUser().getId(), result.get(1).getUser().getId());
        assertEquals(comment2.getText(), result.get(1).getText());
        assertEquals(comment2.getRating(), result.get(1).getRating());
        assertEquals(comment2.getStatus(), result.get(1).getStatus());
    }

    @Test
    void shouldNotGetAllByEventWithNegativeFrom() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getAllByEvent(event.getId(), -15, 15));
    }

    @Test
    void shouldNotGetAllByEventWithNegativeSize() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getAllByEvent(event.getId(), 0, -15));
    }

    @Test
    void shouldNotGetAllByEventWithZeroSize() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getAllByEvent(event.getId(), 0, 0));
    }

    @Test
    void shouldGetAllByUserId() {
        var comment2 = service.create(user.getId(), event2.getId(), newCommentDtoForUser2);

        var result = service.getAllByUser(user.getId(), 0, 15);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(comment.getId(), result.get(0).getId());
        assertEquals(comment.getEvent().getId(), result.get(0).getEvent().getId());
        assertEquals(comment.getUser().getId(), result.get(0).getUser().getId());
        assertEquals(comment.getText(), result.get(0).getText());
        assertEquals(comment.getRating(), result.get(0).getRating());
        assertEquals(comment.getStatus(), result.get(0).getStatus());

        assertEquals(comment2.getId(), result.get(1).getId());
        assertEquals(comment2.getEvent().getId(), result.get(1).getEvent().getId());
        assertEquals(comment2.getUser().getId(), result.get(1).getUser().getId());
        assertEquals(comment2.getText(), result.get(1).getText());
        assertEquals(comment2.getRating(), result.get(1).getRating());
        assertEquals(comment2.getStatus(), result.get(1).getStatus());
    }

    @Test
    void shouldNotGetAllByUserWithNegativeFrom() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getAllByUser(user.getId(), -15, 15));
    }

    @Test
    void shouldNotGetAllByUserWithNegativeSize() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getAllByUser(user.getId(), 0, -15));
    }

    @Test
    void shouldNotGetAllByUserWithZeroSize() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getAllByUser(user.getId(), 0, 0));
    }
}