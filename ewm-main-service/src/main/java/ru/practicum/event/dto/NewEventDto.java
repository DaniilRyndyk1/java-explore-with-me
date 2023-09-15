package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.location.dto.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NewEventDto {
    @NotEmpty
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;

    @NotEmpty
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotEmpty
    @NotBlank
    @Size(min = 19, max = 19)
    private String eventDate;

    @NotNull
    private LocationDto location;
    private Boolean paid = false;
    private Long participantLimit = 0L;
    private Boolean requestModeration = true;

    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
