package ru.practicum.participationRequest.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Utils;
import ru.practicum.event.model.Event;
import ru.practicum.participationRequest.enums.ParticipationRequestState;
import ru.practicum.participationRequest.model.ParticipationRequest;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class ParticipationRequestMapperTest {
    private final ParticipationRequestMapper mapper;

    private final ParticipationRequest request = new ParticipationRequest(
            1L,
            LocalDateTime.now(),
            new Event(),
            new User(),
            ParticipationRequestState.WAITING
    );

    @Test
    void shouldConvertParticipationRequestToParticipationRequestDto() {
        var result = mapper.toDto(request);
        assertNotNull(result);
        assertEquals(result.getId(), request.getId());
        assertEquals(result.getRequester(), request.getRequester().getId());
        assertEquals(result.getEvent(), request.getEvent().getId());
        assertEquals(result.getCreated(), request.getCreated().format(Utils.dateTimeFormatter));
        assertEquals(result.getStatus(), request.getStatus());
    }
}
