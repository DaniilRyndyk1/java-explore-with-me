package ru.practicum.user.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.Utils;
import ru.practicum.user.model.User;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase
public class UserRepositoryTest {
    @Autowired
    private UserRepository repository;
    private final String name1 = "Arnold";
    private final String name2 = "arni@yandex.ru";
    private final String email1 = "Danil";
    private final String email2 = "danil@yandex.ru";

    private User user1 =  new User(-1L, name1, email1);
    private User user2 =  new User(-1L, name2, email2);

    @BeforeEach
    void setUp() {
        user1 = repository.save(user1);
        user2 = repository.save(user2);
    }

    @Test
    void shouldFindAllByIdIn() {
        var ids = new Long[] {user1.getId(), user2.getId()};
        var page = Utils.getPageRequest(0, 10);
        var result = repository.findAllByIdIn(ids, page).stream().collect(Collectors.toList());
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(user1.getId(), result.get(0).getId());
        assertEquals(user1.getName(), result.get(0).getName());
        assertEquals(user1.getEmail(), result.get(0).getEmail());
        assertEquals(user2.getId(), result.get(1).getId());
        assertEquals(user2.getName(), result.get(1).getName());
        assertEquals(user2.getEmail(), result.get(1).getEmail());
    }
}