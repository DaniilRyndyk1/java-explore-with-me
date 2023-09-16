package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentAdminRequest;
import ru.practicum.comment.dto.UpdateCommentUserRequest;
import ru.practicum.comment.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CommentController {
    private final CommentService service;

    @GetMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CommentDto getById(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        return service.getDtoByIds(userId, commentId);
    }

    @GetMapping("/users/{userId}/comments")
    @ResponseStatus(code = HttpStatus.OK)
    public List<CommentDto> getAllByUser(@PathVariable Long userId,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        return service.getAllByUser(userId, from, size);
    }

    @GetMapping("/events/{eventId}/comments")
    @ResponseStatus(code = HttpStatus.OK)
    public List<CommentDto> getAllByEvent(@PathVariable Long eventId,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "10") Integer size) {
        return service.getAllByEvent(eventId, from, size);
    }

    @PostMapping("/events/{eventId}/comments/{userId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CommentDto create(@PathVariable Long eventId,
                             @PathVariable Long userId,
                             @Valid @RequestBody NewCommentDto dto) {
        return service.create(userId, eventId, dto);
    }

    @DeleteMapping("/events/{eventId}/comments/{userId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long eventId,
                       @PathVariable Long userId) {
        service.delete(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/comments/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CommentDto update(@PathVariable Long eventId,
                             @PathVariable Long userId,
                             @Valid @RequestBody UpdateCommentUserRequest dto) {
        return service.update(userId, eventId, dto);
    }

    @PatchMapping("/admin/events/{eventId}/comments/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CommentDto updateByAdmin(@PathVariable Long eventId,
                                    @PathVariable Long userId,
                                    @Valid @RequestBody UpdateCommentAdminRequest dto) {
        return service.updateByAdmin(userId, eventId, dto);
    }
}