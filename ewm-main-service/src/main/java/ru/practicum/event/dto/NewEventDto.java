package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.location.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NewEventDto {
    @Size(min = 20, max = 2000)
    @NotBlank
    private String annotation;

    @NotNull
    private Long category;

    @Size(min = 20, max = 7000)
    @NotNull
    private String description;

    @Size(min = 19, max = 19)
    @NotNull
    private String eventDate;

    @NotNull
    private Location location;

    private Boolean paid = false;
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;

    @Size(min = 3, max = 120)
    @NotNull
    private String title;
}
