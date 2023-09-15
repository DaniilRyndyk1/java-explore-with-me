package ru.practicum.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.comment.enums.CommentStatus;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.UserShortDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentDto {
    private Long id;
    private EventShortDto event;
    private UserShortDto user;
    private String text;
    private Integer rating;
    private CommentStatus status;
}
