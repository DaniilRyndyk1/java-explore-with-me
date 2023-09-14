package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.Set;

@Transactional
public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiator_Id(Long userId, Pageable pageRequest);
    Set<Event> findAllByIdIn(Set<Long> ids);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE (e.initiator.id IN :ids OR :idsSize = 0) " +
            "AND (e.state IN :states OR :statesSize = 0) " +
            "AND (e.category.id IN :categories OR :categoriesSize = 0) " +
            "AND e.eventDate >= :start " +
            "AND e.eventDate <= :end " +
            "ORDER BY e.id")
    Page<Event> findAllByAdminParams (
            Long[] ids,
            EventState[] states,
            Long[] categories,
            LocalDateTime start,
            LocalDateTime end,
            Integer idsSize,
            Integer statesSize,
            Integer categoriesSize,
            Pageable pageRequest);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE e.state = 'PUBLISHED' " +
            "AND (LOWER(e.annotation) LIKE %:text% " +
            "OR LOWER(e.description) LIKE %:text% " +
            "OR :isTextNull = true) " +
            "AND (e.paid = :paid OR :isPaidNull = true)" +
            "AND e.eventDate >= :start " +
            "AND e.eventDate <= :end " +
            "AND (e.category.id IN :categories OR :categoriesSize = 0) " +
            "ORDER BY e.id")
    Page<Event> findAllByUserParams (
            String text,
            Long[] categories,
            Boolean paid,
            LocalDateTime start,
            LocalDateTime end,
            Boolean isPaidNull,
            Boolean isTextNull,
            Integer categoriesSize,
            Pageable pageRequest);

    @Transactional
    @Query("UPDATE Event e SET e.views = :value WHERE e.id = :id")
    @Modifying(clearAutomatically = true)
    void setViewsById(Long id, Long value);
}