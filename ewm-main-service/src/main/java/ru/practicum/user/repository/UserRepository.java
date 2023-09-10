package ru.practicum.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<UserDto> findAllByIdIn(Long[] ids, Pageable pageRequest);
}
