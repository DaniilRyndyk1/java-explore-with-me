package ru.practicum.user.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class UserMapperTest {
    private final UserMapper mapper;
    private final Long id = 10L;
    private final String name = "Arnold";
    private final String email = "arni@yandex.ru";
    private final User user = new User(id, name, email);
    private final NewUserRequest userRequest = new NewUserRequest(name, email);

    @Test
    void shouldConvertNewUserRequestToUser() {
        var result = mapper.toUser(user.getId(), userRequest);
        assertEquals(result.getId(), user.getId());
        assertEquals(result.getName(), userRequest.getName());
        assertEquals(result.getEmail(), userRequest.getEmail());
    }

    @Test
    void shouldConvertUserToUserDto() {
        var result = mapper.toUserDto(user);
        assertEquals(result.getId(), user.getId());
        assertEquals(result.getName(), user.getName());
        assertEquals(result.getEmail(), user.getEmail());
    }

    @Test
    void shouldConvertUserToUserShortDto() {
        var result = mapper.toUserShortDto(user);
        assertEquals(result.getId(), user.getId());
        assertEquals(result.getName(), user.getName());
    }
}
