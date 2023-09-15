package ru.practicum.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateCommentUserRequest {
    @Size(min = 1, max = 1000)
    private String text;

    @Min(1)
    @Max(5)
    private Integer rating;
}