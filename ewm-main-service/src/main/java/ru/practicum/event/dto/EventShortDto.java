package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
public class EventShortDto {
    private Long id;

    @NotEmpty
    @NotBlank
    private String annotation;

    @NotNull
    private CategoryDto category;
    private Long confirmedRequests;

    @NotEmpty
    @NotBlank
    @Size(min = 19, max = 19)
    private String eventDate;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private Boolean paid;

    @NotEmpty
    @NotBlank
    private String title;
    private Long views;
}
