package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.Utils;
import ru.practicum.handler.EntityNotFoundException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public UserDto add(@NotNull NewUserRequest dto) {
        return mapper.toUserDto(repository.save(mapper.toUser(dto)));
    }

    public List<UserDto> getAll(List<Long> ids, @NotNull Integer from, @NotNull Integer size) {
        return repository
                .findAllByIdIn(ids, Utils.getPageRequest(from, size))
                .stream()
                .collect(Collectors.toList());
    }

    public void delete(@NotNull Long id) {
        checkExistsById(id);
        repository.deleteById(id);
    }

    public User getById(@NotNull Long userId) {
        return repository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id=" + userId + " was not found"));
    }

    public void checkExistsById(@NotNull Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("User with id=" + id + " was not found");
        }
    }
}
