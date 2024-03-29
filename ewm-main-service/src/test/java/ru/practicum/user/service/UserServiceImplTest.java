package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.handler.EntityNotFoundException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class UserServiceImplTest {
    private final UserServiceImpl service;
    private final String name1 = "Arnold";
    private final String name2 = "arni@yandex.ru";
    private final String email1 = "Danil";
    private final String email2 = "danil@yandex.ru";
    private final NewUserRequest newUserRequest1 = new NewUserRequest(name1, email1);
    private final NewUserRequest newUserRequest2 = new NewUserRequest(name2, email2);

    private UserDto user1;

    @BeforeEach
    void setup() {
        user1 = service.add(newUserRequest1);
    }

    @Test
    void shouldAddUser() {
        var newObject = service.add(newUserRequest2);
        assertEquals(newUserRequest2.getName(), newObject.getName());
        assertEquals(newUserRequest2.getEmail(), newObject.getEmail());
    }

    @Test
    void shouldGetAll() {
        UserDto user2 = service.add(newUserRequest2);
        var ids = List.of(user1.getId(), user2.getId());
        var users = service.getAll(ids, 0, 10);
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals(user1.getId(), users.get(0).getId());
        assertEquals(user1.getName(), users.get(0).getName());
        assertEquals(user1.getEmail(), users.get(0).getEmail());
        assertEquals(user2.getId(), users.get(1).getId());
        assertEquals(user2.getName(), users.get(1).getName());
        assertEquals(user2.getEmail(), users.get(1).getEmail());
    }

    @Test
    void shouldDelete() {
        service.delete(user1.getId());
        assertThrows(EntityNotFoundException.class,
                () -> service.getById(user1.getId())
        );
    }

    @Test
    void shouldGetById() {
        var name = "Danil2";
        var email = "danil2@yandex.ru";
        var original = service.add(
                new NewUserRequest(name, email)
        );
        var newObject = service.getById(original.getId());
        assertEquals(original.getId(), newObject.getId());
        assertEquals(original.getName(), newObject.getName());
        assertEquals(original.getEmail(), newObject.getEmail());
    }
}