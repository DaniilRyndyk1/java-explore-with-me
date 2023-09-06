package ru.practicum.statservice.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statservice.dto.EndpointHitInputDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class EndpointHitServiceTest {
    private final EndpointHitService service;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EndpointHitInputDto hit1 =
            new EndpointHitInputDto("ewm-main-service",
                                    "/events/2",
                                    "192.163.0.1",
                                    "2023-06-06 11:00:23");
    private final EndpointHitInputDto hit2 =
            new EndpointHitInputDto("ewm-main-service",
                    "/events/2",
                    "192.163.0.1",
                    "2023-06-10 10:00:00");

    private final EndpointHitInputDto hit3 =
            new EndpointHitInputDto("ewm-main-service",
                    "/events/2",
                    "192.163.1.1",
                    "2023-06-15 14:00:00");

    private final EndpointHitInputDto hit4 =
            new EndpointHitInputDto("ewm-main-service",
                    "/events/1",
                    "192.163.1.1",
                    "2023-06-10 10:00:00");

    @BeforeEach
    void setup() {
        service.create(hit1);
        service.create(hit2);
        service.create(hit3);
    }

    @Test
    void shouldCreateEndpointHit() {
        var originalSize = service.getAllByParams(
                LocalDateTime.parse("2023-01-01 00:00:00", formatter),
                LocalDateTime.parse("2023-09-01 00:00:00", formatter),
                null,
                false).size();
        service.create(hit4);
        var newSize = service.getAllByParams(
                LocalDateTime.parse("2023-01-01 00:00:00", formatter),
                LocalDateTime.parse("2023-09-01 00:00:00", formatter),
                null,
                false).size();
        assertEquals(originalSize + 1, newSize);
    }

    @Test
    void shouldFindZeroHitsWithOldDate() {
        var hits = service.getAllByParams(
                LocalDateTime.parse("2022-01-01 00:00:00", formatter),
                LocalDateTime.parse("2022-09-01 00:00:00", formatter),
                null,
                false);
        assertEquals(0, hits.size());
    }

    @Test
    void shouldFindTwoUniqueHitsToUri() {
        var uris = new String[] {"/events/2"};
        var hits = service.getAllByParams(
                LocalDateTime.parse("2023-01-01 00:00:00", formatter),
                LocalDateTime.parse("2023-09-01 00:00:00", formatter),
                uris,
                true);
        assertEquals(1, hits.size());
        assertEquals(2, hits.get(0).getHits());
    }

    @Test
    void shouldFindThreeHitsToUri() {
        var uris = new String[] {"/events/2"};
        var hits = service.getAllByParams(
                LocalDateTime.parse("2023-01-01 00:00:00", formatter),
                LocalDateTime.parse("2023-09-01 00:00:00", formatter),
                uris,
                false);
        assertEquals(1, hits.size());
        assertEquals(3, hits.get(0).getHits());
    }

    @Test
    void shouldFindHits() {
        service.create(hit4);

        var hits = service.getAllByParams(
                LocalDateTime.parse("2023-01-01 00:00:00", formatter),
                LocalDateTime.parse("2023-09-01 00:00:00", formatter),
                null,
                false);
        assertEquals(2, hits.size());
        assertEquals(3, hits.get(0).getHits());
        assertEquals(1, hits.get(1).getHits());
    }

    @Test
    void shouldFindUniqueHits() {
        service.create(hit4);

        var hits = service.getAllByParams(
                LocalDateTime.parse("2023-01-01 00:00:00", formatter),
                LocalDateTime.parse("2023-09-01 00:00:00", formatter),
                null,
                true);
        assertEquals(2, hits.size());
        assertEquals(2, hits.get(0).getHits());
        assertEquals(1, hits.get(1).getHits());
    }
}