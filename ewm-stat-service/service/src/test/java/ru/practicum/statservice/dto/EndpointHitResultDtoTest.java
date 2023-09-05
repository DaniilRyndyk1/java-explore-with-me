package ru.practicum.statservice.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class EndpointHitResultDtoTest {
    @Autowired
    private JacksonTester<EndpointHitResultDto> json;
    private final EndpointHitResultDto dto = new EndpointHitResultDto("app", "/hit", 1L);

    @Test
    void testJsonEndpointHitResultDto() throws Exception {
        JsonContent<EndpointHitResultDto> result = json.write(dto);
        assertThat(result).extractingJsonPathStringValue("$.app").isEqualTo(dto.getApp());
        assertThat(result).extractingJsonPathStringValue("$.uri").isEqualTo(dto.getUri());
        assertThat(result).extractingJsonPathNumberValue("$.hits").isEqualTo(dto.getHits().intValue());
    }

    @Test
    void testJsonEmptyEndpointHitResultDto() throws Exception {
        JsonContent<EndpointHitResultDto> result = json.write(new EndpointHitResultDto());
        assertThat(result).extractingJsonPathStringValue("$.app").isNullOrEmpty();
        assertThat(result).extractingJsonPathStringValue("$.uri").isNullOrEmpty();
        assertThat(result).extractingJsonPathNumberValue("$.hits").isNull();
    }
}
