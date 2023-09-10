package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiator_Id(Long userId, Pageable pageRequest);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE e.initiator.id IN :ids " +
            "AND e.state IN :states " +
            "AND e.category.id IN :categories " +
            "AND e.eventDate >= :start " +
            "AND e.eventDate <= :end ")
    Page<Event> findAllByAdminParams (
            Long[] ids,
            String[] states,
            Long[] categories,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageRequest);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE e.state = 'PUBLISHED' " +
            "AND (LOWER(e.annotation) like '*:text*' " +
            "OR LOWER(e.description) like '*:text*') " +
            "AND e.paid = :paid " +
            "AND e.eventDate >= :start " +
            "AND e.eventDate <= :end " +
            "AND e.category.id IN :categories ")
    Page<Event> findAllByUserParams (
            String text,
            Long[] categories,
            Boolean paid,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageRequest);
}