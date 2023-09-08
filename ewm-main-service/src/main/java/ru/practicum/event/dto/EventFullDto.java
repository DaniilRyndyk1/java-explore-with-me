package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.enums.EventState;
import ru.practicum.user.dto.UserShortDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EventFullDto {
    private Long id;

    @NotNull
    private String annotation;

    @NotNull
    private CategoryDto category;
    private Long confirmedRequests;

    @Size(min = 19, max = 19)
    private String createdOn;
    private String description;

    @NotNull
    private LocalDateTime eventDate;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private Location location;

    @NotNull
    private Boolean paid;
    private Integer participantLimit = 0;

    @Size(min = 19, max = 19)
    private String publishedOn;
    private Boolean requestModeration = true;
    private EventState state;

    @NotNull
    private String title;
    private Long views;
}
