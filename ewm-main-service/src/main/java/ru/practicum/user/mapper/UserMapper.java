package ru.practicum.user.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

@Component
@AllArgsConstructor
public class UserMapper {
    public User toUser(Long id, NewUserRequest dto) {
        return new User(
                id,
                dto.getName(),
                dto.getEmail());
    }

    public User toUser(NewUserRequest dto) {
        return new User(
                -1L,
                dto.getName(),
                dto.getEmail());
    }

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

    public UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName());
    }
}
