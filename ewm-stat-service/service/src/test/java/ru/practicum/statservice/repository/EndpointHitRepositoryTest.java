package ru.practicum.statservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.statservice.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class EndpointHitRepositoryTest {
    @Autowired
    private EndpointHitRepository repository;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Long hitId = 1L;

    private EndpointHit hit;

    @BeforeEach
    void setUp() {
        hit = new EndpointHit(hitId,
                "ewm-main-service",
                "/events/2",
                "192.163.0.1",
                LocalDateTime.parse("2023-06-06 11:00:23", formatter));
        hit = repository.save(hit);
        hitId++;

        hit = new EndpointHit(hitId,
                "ewm-main-service",
                "/events/2",
                "192.163.0.1",
                LocalDateTime.parse("2023-06-07 11:00:23", formatter));
        hit = repository.save(hit);
        hitId++;
    }

    @Test
    void shouldFindOneUniqueHit() {
        var items = repository.findUniqueByPeriod(
                LocalDateTime.parse("2023-01-01 00:00:00", formatter),
                LocalDateTime.parse("2023-09-01 00:00:00", formatter)
        );
        assertEquals(1, items.size());
        assertEquals(1, items.get(0).getHits());
    }

    @Test
    void shouldFindOneUniqueHitByUri() {
        hit = new EndpointHit(hitId,
                "ewm-main-service",
                "/events/1",
                "192.163.0.1",
                LocalDateTime.parse("2023-06-07 11:00:23", formatter));
        hit = repository.save(hit);
        hitId++;

        var items = repository.findUniqueByPeriodAndUriIn(
                LocalDateTime.parse("2023-01-01 00:00:00", formatter),
                LocalDateTime.parse("2023-09-01 00:00:00", formatter),
                new String[] {"/events/2"}
        );
        assertEquals(1, items.size());
        assertEquals(1, items.get(0).getHits());
    }

    @Test
    void shouldFindTwoHits() {
        var items = repository.findByPeriod(
                LocalDateTime.parse("2023-01-01 00:00:00", formatter),
                LocalDateTime.parse("2023-09-01 00:00:00", formatter)
        );
        assertEquals(1, items.size());
        assertEquals(2, items.get(0).getHits());
    }

    @Test
    void shouldFindTwoHitsByUri() {
        hit = new EndpointHit(hitId,
                "ewm-main-service",
                "/events/1",
                "192.163.0.1",
                LocalDateTime.parse("2023-06-07 11:00:23", formatter));
        hit = repository.save(hit);
        hitId++;

        var items = repository.findByPeriodAndUriIn(
                LocalDateTime.parse("2023-01-01 00:00:00", formatter),
                LocalDateTime.parse("2023-09-01 00:00:00", formatter),
                new String[] {"/events/2"}
        );
        assertEquals(1, items.size());
        assertEquals(2, items.get(0).getHits());
    }
}
