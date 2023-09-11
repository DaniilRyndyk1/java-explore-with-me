package ru.practicum.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UserService service;
    @Autowired
    private MockMvc mvc;

    private final String name = "derekzuu";
    private final String email = "abobus@yandex.ru";

    private final NewUserRequest newUserRequest = new NewUserRequest(name, email);
    private final UserDto userDto = new UserDto(10L, name, email);
    private final UserDto userDto2 = new UserDto(15L, name + "!", email + "ndex.ru");

    @Test
    void shouldCreate() throws Exception {
        when(service.add(any(NewUserRequest.class)))
                .thenReturn(userDto);

        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(newUserRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void shouldNotCreateWithBlankName() throws Exception {
        var tempRequest = new NewUserRequest("", newUserRequest.getEmail() + "uu");

        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(tempRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRemove() throws Exception {
        mvc.perform(delete("/admin/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetAll() throws Exception {
        when(service.getAll(any(Long[].class), any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(userDto, userDto2));

        mvc.perform(get("/admin/users?ids=1,2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$.[0].email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.[1].id", is(userDto2.getId()), Long.class))
                .andExpect(jsonPath("$.[1].name", is(userDto2.getName())))
                .andExpect(jsonPath("$.[1].email", is(userDto2.getEmail())));
    }

    @Test
    void shouldNotGetAllWithoutIds() throws Exception {
        mvc.perform(get("/admin/users?from=0&size=10").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
