package ru.practicum.event.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.enums.EventState;
import ru.practicum.location.model.Location;
import ru.practicum.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RequiredArgsConstructor
@Getter
public class EventFullDto {
    private final Long id;

    @NotEmpty
    @NotBlank
    private final String annotation;

    @NotNull
    private final CategoryDto category;
    private final Long confirmedRequests;

    @NotEmpty
    @NotBlank
    @Size(min = 19, max = 19)
    private final String createdOn;

    @NotEmpty
    @NotBlank
    private final String description;

    @NotEmpty
    @NotBlank
    @Size(min = 19, max = 19)
    private final String eventDate;

    @NotNull
    private final UserShortDto initiator;

    @NotNull
    private final Location location;

    @NotNull
    private final Boolean paid;
    private final Integer participantLimit = 0;

    @NotEmpty
    @NotBlank
    @Size(min = 19, max = 19)
    private final String publishedOn;
    private final Boolean requestModeration = true;
    private final EventState state;

    @NotEmpty
    @NotBlank
    private final String title;
    private final Long views;
}
