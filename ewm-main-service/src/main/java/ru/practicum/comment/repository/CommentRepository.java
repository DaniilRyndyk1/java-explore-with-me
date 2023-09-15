package ru.practicum.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.model.Comment;

import java.util.List;
import java.util.Optional;

@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findFirstByUser_idAndEvent_id(Long userId, Long eventId);
    @Query("SELECT c " +
            "FROM Comment c " +
            "WHERE c.status != 'REJECTED' " +
            "AND c.event.id = :eventId")
    List<Comment> findAllByEvent_id(Long eventId, Pageable pageable);
    @Query("SELECT c " +
            "FROM Comment c " +
            "WHERE c.status != 'REJECTED' " +
            "AND c.user.id = :userId")
    List<Comment> findAllByUser_id(Long userId, Pageable pageable);
    @Query("SELECT c.id " +
           "FROM Comment c " +
           "WHERE c.event.id = :eventId " +
           "AND c.user.id = :userId")
    Long findIdByUser_idAndEvent_id(Long userId, Long eventId);
    @Query("SELECT count(c.id) > 0 " +
            "FROM Comment c " +
            "WHERE c.user.id = :userId " +
            "AND c.event.id = :eventId")
    Boolean existsByUser_idAndEvent_id(Long userId, Long eventId);
}