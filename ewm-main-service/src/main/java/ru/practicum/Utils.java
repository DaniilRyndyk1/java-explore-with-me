package ru.practicum;

import org.springframework.data.domain.PageRequest;

import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static PageRequest getPageRequest(@NotNull Integer from, @NotNull Integer size) {
        if (from < 0) {
            throw new IllegalArgumentException("Неверное значение параметра from");
        } else if (size <= 0) {
            throw new IllegalArgumentException("Неверное значение параметра size");
        }

        return PageRequest.of(from / size, size);
    }
}
