package ru.practicum.user.service;

import org.springframework.data.domain.Page;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import javax.validation.constraints.NotNull;

public interface UserService {
    UserDto add(NewUserRequest dto);
    Page<UserDto> getAll(Long[] ids, Integer from, Integer size);
    void delete(Long userId);
    User getById(@NotNull Long userId);
}
