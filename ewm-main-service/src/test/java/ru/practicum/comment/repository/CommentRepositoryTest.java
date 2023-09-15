package ru.practicum.comment.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.Utils;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.comment.enums.CommentStatus;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.event.TestData.*;


@DataJpaTest
@AutoConfigureTestDatabase
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository repository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;
    private User user2;
    private Event event;
    private Comment comment;
    private Comment comment2;

    @BeforeEach
    void setUp() {
        var location = new Location(-1L, locationDto.getLat(), locationDto.getLon());
        location = locationRepository.save(location);

        var category = new Category(-1L, "best");
        category = categoryRepository.save(category);

        user = new User(-1L, "test", "test@ya.ru");
        user = userRepository.save(user);

        user2 = new User(-1L, "test", "test@ya.ru");
        user2 = userRepository.save(user);

        event = eventRepository.save(
                new Event(
                        -1L,
                        annotation,
                        category,
                        0L,
                        LocalDateTime.now(),
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
                        0L
                )
        );

        comment = repository.save(new Comment(-1L, event, user, "Swag", 4, CommentStatus.PENDING));
    }

    @Test
    void shouldFindCommentByUserIdAndEventId() {
        var result = repository.findFirstByUser_idAndEvent_id(user.getId(), event.getId());
        assertNotNull(result);

        var newComment = result.get();

        assertEquals(comment.getId(), newComment.getId());
        assertEquals(comment.getEvent().getId(), newComment.getEvent().getId());
        assertEquals(comment.getUser().getId(), newComment.getUser().getId());
        assertEquals(comment.getText(), newComment.getText());
        assertEquals(comment.getStatus(), newComment.getStatus());
        assertEquals(comment.getRating(), newComment.getRating());
    }

    @Test
    void shouldCheckExistsByUserIdAndEventId() {
        var result = repository.existsByUser_idAndEvent_id(user.getId(), event.getId());
        assertEquals(true, result);
    }

    @Test
    void shouldGetAllByEvent() {
        var page = Utils.getPageRequest(0, 15);
        var result = repository.findAllByEvent_id(event.getId(), page);
        assertNotNull(result);
        assertEquals(1, result.size());

        var newComment = result.get(0);

        assertEquals(comment.getId(), newComment.getId());
        assertEquals(comment.getEvent().getId(), newComment.getEvent().getId());
        assertEquals(comment.getUser().getId(), newComment.getUser().getId());
        assertEquals(comment.getText(), newComment.getText());
        assertEquals(comment.getStatus(), newComment.getStatus());
        assertEquals(comment.getRating(), newComment.getRating());
    }

    @Test
    void shouldGetAllByUserId() {
        var page = Utils.getPageRequest(0, 15);
        var result = repository.findAllByUser_id(user.getId(), page);
        assertNotNull(result);
        assertEquals(1, result.size());

        var newComment = result.get(0);

        assertEquals(comment.getId(), newComment.getId());
        assertEquals(comment.getEvent().getId(), newComment.getEvent().getId());
        assertEquals(comment.getUser().getId(), newComment.getUser().getId());
        assertEquals(comment.getText(), newComment.getText());
        assertEquals(comment.getStatus(), newComment.getStatus());
        assertEquals(comment.getRating(), newComment.getRating());
    }

    @Test
    void shouldFindIdByUserIdAndEventId() {
        var result = repository.findIdByUser_idAndEvent_id(user.getId(), event.getId());
        assertNotNull(result);
        assertEquals(comment.getId(), result);
    }
}