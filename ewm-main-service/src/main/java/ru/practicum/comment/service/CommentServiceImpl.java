package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Utils;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentAdminRequest;
import ru.practicum.comment.dto.UpdateCommentUserRequest;
import ru.practicum.comment.enums.CommentStatus;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.service.EventService;
import ru.practicum.handler.ConflictException;
import ru.practicum.handler.EntityNotFoundException;
import ru.practicum.user.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final CommentMapper mapper;
    private final EventService eventService;
    private final UserService userService;

    public Comment getById(@NotNull Long id) {
        var message = "Comment with id=" + id + " was not found";
        var comment = repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(message)
        );

        if (comment.getStatus().equals(CommentStatus.REJECTED)) {
            throw new EntityNotFoundException(message);
        }

        return comment;
    }

    public CommentDto getDtoByIds(@NotNull Long userId, @NotNull Long eventId) {
        userService.checkExistsById(userId);
        eventService.checkExistsById(eventId);

        return mapper.toCommentDto(getByIds(userId, eventId));
    }

    public List<CommentDto> getAllByEvent(@NotNull Long eventId, @NotNull Integer from, @NotNull Integer size) {
        return repository.findAllByEvent_id(eventId, Utils.getPageRequest(from, size))
                .stream()
                .map(mapper::toCommentDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getAllByUser(@NotNull Long userId, @NotNull Integer from, @NotNull Integer size) {
        return repository.findAllByUser_id(userId, Utils.getPageRequest(from, size))
                .stream()
                .map(mapper::toCommentDto)
                .collect(Collectors.toList());
    }

    public Comment getByIds(@NotNull Long userId, @NotNull Long eventId) {
        var message = "Comment userId=" + userId + " and eventId=" + eventId + " was not found";
        var comment = repository.findFirstByUser_idAndEvent_id(userId, eventId).orElseThrow(
                () -> new EntityNotFoundException(message)
        );

        if (comment.getStatus().equals(CommentStatus.REJECTED)) {
            throw new EntityNotFoundException(message);
        }

        return comment;
    }

    public void checkExistsByIds(@NotNull Long userId, @NotNull Long eventId) {
        if (!repository.existsByUser_idAndEvent_id(userId, eventId)) {
            throw new EntityNotFoundException("Comment userId=" + userId + " and eventId=" + eventId + " was not found");
        }
    }

    @Transactional
    public CommentDto create(@NotNull Long userId,
                             @NotNull Long eventId,
                             @NotNull NewCommentDto dto) {
        var user = userService.getById(userId);
        var event = eventService.getById(eventId);

        if (repository.existsByUser_idAndEvent_id(userId, eventId)) {
            throw new ConflictException("Comment from user " + userId + " exists for event " + eventId);
        }

        var comment = mapper.toComment(-1L, event, user, CommentStatus.PENDING, dto);

        return mapper.toCommentDto(repository.save(comment));
    }

    @Transactional
    public void delete(@NotNull Long userId, @NotNull Long eventId) {
        var comment = getByIds(userId, eventId);

        if (comment.getStatus().equals(CommentStatus.CONFIRMED)) {
            throw new ConflictException("Cannot delete confirmed comment");
        }

        repository.deleteById(comment.getId());
    }

    @Transactional
    public CommentDto update(@NotNull Long userId,
                             @NotNull Long eventId,
                             @NotNull UpdateCommentUserRequest dto) {
        checkExistsByIds(userId, eventId);

        var comment = getByIds(userId, eventId);

        if (!comment.getUser().getId().equals(userId)) {
            throw new EntityNotFoundException("Comment userId=" + userId + " and eventId=" + eventId + " was not found");
        }

        if (dto.getText() != null) {
            comment.setText(dto.getText());
        }

        if (dto.getRating() != null) {
            comment.setRating(dto.getRating());
        }

        comment.setStatus(CommentStatus.PENDING);

        return mapper.toCommentDto(repository.save(comment));
    }

    @Transactional
    public CommentDto updateByAdmin(@NotNull Long userId,
                                    @NotNull Long eventId,
                                    @NotNull UpdateCommentAdminRequest dto) {
        checkExistsByIds(userId, eventId);

        var comment = getByIds(userId, eventId);

        if (dto.getText() != null) {
            comment.setText(dto.getText());
        }

        if (dto.getRating() != null) {
            comment.setRating(dto.getRating());
        }

        if (dto.getNewStatus() != null) {
            comment.setStatus(dto.getNewStatus());
        }

        return mapper.toCommentDto(repository.save(comment));
    }
}