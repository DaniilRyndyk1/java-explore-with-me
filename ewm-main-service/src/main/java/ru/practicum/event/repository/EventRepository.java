package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.model.Event;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


public interface EventRepository extends JpaRepository<Event, Long> {
    @Transactional(readOnly = true)
    Page<Event> findAllByInitiator_Id(Long userId, Pageable pageRequest);
    @Transactional(readOnly = true)
    Set<Event> findAllByIdIn(Set<Long> ids);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE (e.initiator.id IN :ids OR coalesce(:ids, null) IS null) " +
            "AND (e.state IN :states OR coalesce(:states, null) IS null) " +
            "AND (e.category.id IN :categories OR coalesce(:categories, null) IS null) " +
            "AND e.eventDate >= :start " +
            "AND e.eventDate <= :end " +
            "ORDER BY e.id")
    @Transactional(readOnly = true)
    Page<Event> findAllByAdminParams (
            List<Long> ids,
            List<EventState> states,
            List<Long> categories,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageRequest);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE e.state = 'PUBLISHED' " +
            "AND (LOWER(e.annotation) LIKE %:text% " +
            "OR LOWER(e.description) LIKE %:text% " +
            "OR :text = '') " +
            "AND (e.paid = :paid OR :paid IS null)" +
            "AND e.eventDate >= :start " +
            "AND e.eventDate <= :end " +
            "AND (e.category.id IN :categories OR coalesce(:categories, null) is null) " +
            "ORDER BY e.id")
    @Transactional(readOnly = true)
    Page<Event> findAllByUserParams (
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageRequest);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("UPDATE Event e SET e.views = :value WHERE e.id = :id")
    @Modifying(clearAutomatically = true)
    void setViewsById(Long id, Long value);
}