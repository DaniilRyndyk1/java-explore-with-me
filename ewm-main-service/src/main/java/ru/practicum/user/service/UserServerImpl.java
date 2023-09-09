package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.handler.NotFoundException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class UserServerImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public UserDto add(@NotNull NewUserRequest dto) {
        var user = mapper.toUser(-1L, dto);
        user = repository.save(user);
        return mapper.toUserDto(user);
    }

    public Page<UserDto> getAll(Long[] ids, @NotNull Integer from, @NotNull Integer size) {
        if (from < 0 || size <= 0) {
            throw new IllegalArgumentException("Неверное значение параметра");
        }

        return repository.findAllByIdIn(ids, PageRequest.of(from / size, size));
    }

    public void delete(@NotNull Long userId) {
        var user = repository.getById(userId);
        repository.delete(user);
    }

    public User getById(@NotNull Long userId) {
        var userOptional = repository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("DEBUG DEBUG DEBUG"); // TODO дописать
        }
        return userOptional.get();
    }
}
