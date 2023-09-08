package ru.practicum.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<UserDto> findAllByUser_IdIn(Long[] ids, PageRequest pageRequest);
}
