package ru.practicum.statservice.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ActiveProfiles("test")
public class EndpointHitInputDtoTest {
    @Autowired
    private JacksonTester<EndpointHitInputDto> json;
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final LocalDateTime created = LocalDateTime.parse("2023-06-02 00:00:00", formatter);
    private final EndpointHitInputDto dto =
            new EndpointHitInputDto("app", "/hit", "192.168.1.1", created.format(formatter));

    @Test
    void testEndpointHitInputDto() throws Exception {
        JsonContent<EndpointHitInputDto> result = json.write(dto);
        assertThat(result).extractingJsonPathStringValue("$.app").isEqualTo("app");
        assertThat(result).extractingJsonPathStringValue("$.uri").isEqualTo("/hit");
        assertThat(result).extractingJsonPathStringValue("$.ip").isEqualTo("192.168.1.1");
        assertThat(result).extractingJsonPathStringValue("$.timestamp").isEqualTo(created.format(formatter));
    }
}
