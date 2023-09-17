package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentAdminRequest;
import ru.practicum.comment.dto.UpdateCommentUserRequest;
import ru.practicum.comment.model.Comment;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface CommentService {
    CommentDto getDtoByIds(@NotNull Long userId,
                           @NotNull Long commentId);
    Comment getById(@NotNull Long id);
    void checkExistsByIds(@NotNull Long userId, @NotNull Long eventId);
    CommentDto create(@NotNull Long userId,
                      @NotNull Long eventId,
                      @NotNull NewCommentDto dto);
    void delete(@NotNull Long userId, @NotNull Long eventId);
    CommentDto update(@NotNull Long userId,
                      @NotNull Long eventId,
                      @NotNull UpdateCommentUserRequest dto);
    CommentDto updateByAdmin(@NotNull Long userId,
                             @NotNull Long eventId,
                             @NotNull UpdateCommentAdminRequest dto);
    List<CommentDto> getAllByEvent(@NotNull Long eventId, @NotNull Integer from, @NotNull Integer size);
    List<CommentDto> getAllByUser(@NotNull Long userId, @NotNull Integer from, @NotNull Integer size);
}
