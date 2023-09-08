package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.ParticipationRequestDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {
    private final UserService service;
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("users/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        throw new RuntimeException("Метод не реализован");
    }

    @PostMapping("users/{userId}/requests")
    public ParticipationRequestDto create(@PathVariable Long userId,
                                          @RequestParam Long eventId) {
        throw new RuntimeException("Метод не реализован");
    }

    @PatchMapping("users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable Long userId,
                                          @PathVariable Long requestId) {
        throw new RuntimeException("Метод не реализован");
    }

    @GetMapping("admin/users")
    public List<UserDto> getUsers(@RequestParam Long[] ids,
                                  @RequestParam(defaultValue = "0") Integer from,
                                  @RequestParam(defaultValue = "10") Integer size) {
        throw new RuntimeException("Метод не реализован");
    }

    @PostMapping("admin/users")
    public UserDto addUser(@RequestBody NewUserRequest dto) {
        throw new RuntimeException("Метод не реализован");
    }

    @DeleteMapping("admin/users/{userId}")
    public void delete(@PathVariable Long userId) {

    }
}

