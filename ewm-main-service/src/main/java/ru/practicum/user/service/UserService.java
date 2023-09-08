package ru.practicum.user.service;

import org.springframework.data.domain.Page;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

public interface UserService {
    UserDto add(NewUserRequest dto);
    Page<UserDto> getAll(Long[] ids, Integer from, Integer size);
    void delete(Long userId);
}
