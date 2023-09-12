package ru.practicum.event;

import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.event.enums.EventState;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.user.dto.NewUserRequest;

import java.time.LocalDateTime;

import static ru.practicum.Utils.dateTimeFormatter;


public class TestData {
    public static final String annotation = "annotation___________________________";
    public static final String username = "DanilaSuper";
    public static final String email = "DanilaSuper@yandex.ru";
    public static final String description = "description_________________________";
    public static final String eventDateRaw = "2023-09-20 19:25:55";
    public static final String publishedOnRaw = "2023-09-20 02:25:55";
    public static final LocalDateTime eventDate = LocalDateTime.parse(eventDateRaw, dateTimeFormatter);
    public static final LocalDateTime publishedOn = LocalDateTime.parse(publishedOnRaw, dateTimeFormatter);
    public static final EventState state = EventState.PENDING;
    public static final String title = "hello darkness my friend......";
    public static final Boolean paid = false;
    public static final Boolean requestModeration = false;
    public static final Long participantLimit = 0L;
    public static final LocationDto locationDto = new LocationDto(15L, 20L);
    public static final String categoryName = "Stupid actions";
    public static final NewCategoryDto newCategoryDto = new NewCategoryDto(categoryName);
    public static final NewUserRequest newUserRequest = new NewUserRequest(username, email);
}
