package ru.practicum.comment.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.enums.CommentStatus;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final EventMapper eventMapper;
    private final UserMapper userMapper;
    public Comment toComment(Long id,
                              Event event,
                              User user,
                              CommentStatus status,
                              NewCommentDto dto) {
        return new Comment(
                id,
                event,
                user,
                dto.getText(),
                dto.getRating(),
                status
        );
    }

//    public Category toCategory(Long id, CategoryDto dto) {
//        return new Category(id, dto.getName());
//    }

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                eventMapper.toShortDto(comment.getEvent()),
                userMapper.toUserShortDto(comment.getUser()),
                comment.getText(),
                comment.getRating(),
                comment.getStatus()
        );
    }
}
