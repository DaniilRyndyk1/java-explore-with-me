package ru.practicum.user.service;

import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserService {
    UserDto add(NewUserRequest dto);
    List<UserDto> getAll(List<Long> ids, Integer from, Integer size);
    void delete(Long userId);
    User getById(@NotNull Long userId);
    void checkExistsById(@NotNull Long id);
}
