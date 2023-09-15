package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.enums.EventState;
import ru.practicum.location.model.Location;
import ru.practicum.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EventFullDto {
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
    private String createdOn;

    @NotEmpty
    @NotBlank
    private String description;

    @NotEmpty
    @NotBlank
    @Size(min = 19, max = 19)
    private String eventDate;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private Location location;

    @NotNull
    private Boolean paid;
    private Long participantLimit;

    @NotEmpty
    @NotBlank
    @Size(min = 19, max = 19)
    private String publishedOn;
    private Boolean requestModeration;
    private EventState state;

    @NotEmpty
    @NotBlank
    private String title;
    private Long views;
}
