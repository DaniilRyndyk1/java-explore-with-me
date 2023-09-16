package ru.practicum.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NewCommentDto {
    @Size(min = 1, max = 1000)
    private String text;

    @Min(1)
    @Max(5)
    @NotNull
    private Integer rating;
}
