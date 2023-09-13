package ru.practicum.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT new ru.practicum.user.dto.UserDto(u.id, u.name, u.email) " +
            "FROM User u " +
            "WHERE (u.id IN :ids OR idsLength = 0)")
    Page<UserDto> findAllByIdIn(Long[] ids, Integer idsLength, Pageable pageRequest);
}
