package ru.practicum.statservice.handler;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class ErrorHandlerTest {
    private final ErrorHandler handler;
    @Test
    void shouldCreateEndpointHit() {
        var response = handler.handlerDateTimeParseException(new DateTimeParseException("test", "test", -1));
        assertEquals(response.getError(), "test");
    }
}
