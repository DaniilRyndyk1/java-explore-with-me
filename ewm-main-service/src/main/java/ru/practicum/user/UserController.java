package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Valid
public class UserController {
    private final UserService service;

    @GetMapping("admin/users")
    public Page<UserDto> getAll(@RequestParam Long[] ids,
                                @RequestParam(defaultValue = "0") Integer from,
                                @RequestParam(defaultValue = "10") Integer size) {
        return service.getAll(ids, from, size);
    }

    @PostMapping("admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto add(@RequestBody NewUserRequest dto) {
        return service.add(dto);
    }

    @DeleteMapping("admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        service.delete(userId);
    }
}

