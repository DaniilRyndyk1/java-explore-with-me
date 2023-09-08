package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServerImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public UserDto add(NewUserRequest dto) {
        var user = mapper.toUser(-1L, dto);
        user = repository.save(user);
        return mapper.toUserDto(user);
    }

    public Page<UserDto> getAll(Long[] ids, Integer from, Integer size) {
        var pageRequest = PageRequest.of(from / size, size);
        return repository.findAllByUser_IdIn(ids, pageRequest);
    }

    public void delete(Long userId) {
        var user = repository.getById(userId);
        repository.delete(user);
    }
}
