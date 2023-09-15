package ru.practicum.comment.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.enums.CommentStatus;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.location.model.Location;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.event.TestData.*;
import static ru.practicum.event.TestData.title;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class CommentMapperTest {
    private final CommentMapper mapper;

    private final Event event = new Event(
            15L,
            annotation,
            new Category(-1L, "best"),
            0L,
            LocalDateTime.now(),
            description,
            eventDate,
            new User(-1L, "test", "test@ya.ru"),
            new Location(-1L, locationDto.getLat(), locationDto.getLon()),
            paid,
            0L,
            publishedOn,
            false,
            state,
            title,
            0L
    );
    private final User user = new User(5L, "Arni", "super@ya.ru");

    private final Comment comment = new Comment(
            1L,
            event,
            user,
            "SUPER EVENT FOREVER",
            5,
            CommentStatus.PENDING
    );

    private final NewCommentDto newCommentDto = new NewCommentDto(
            "SUPER EVENT FOREVER",
            5
    );

    @Test
    void shouldConvertNewCommentDtoToComment() {
        var id = 1L;
        var status = CommentStatus.PENDING;
        var result = mapper.toComment(id, event, user, status, newCommentDto);
        assertNotNull(result);
        assertEquals(result.getId(), id);
        assertEquals(result.getEvent().getId(), event.getId());
        assertEquals(result.getUser().getId(), user.getId());
        assertEquals(result.getText(), newCommentDto.getText());
        assertEquals(result.getRating(), newCommentDto.getRating());
        assertEquals(result.getStatus(), status);
    }

    @Test
    void shouldConvertCommentToCommentDto() {
        var result = mapper.toCommentDto(comment);
        assertNotNull(result);
        assertEquals(result.getId(), comment.getId());
        assertEquals(result.getEvent().getId(), comment.getEvent().getId());
        assertEquals(result.getUser().getId(), comment.getUser().getId());
        assertEquals(result.getText(), comment.getText());
        assertEquals(result.getRating(), comment.getRating());
        assertEquals(result.getStatus(), comment.getStatus());
    }
}
