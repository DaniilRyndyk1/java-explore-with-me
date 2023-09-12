package ru.practicum.participationRequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.participationRequest.enums.ParticipationRequestState;
import ru.practicum.participationRequest.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    Optional<ParticipationRequest> findFirstByRequester_IdAndEvent_Id(Long userId, Long eventId);
    List<ParticipationRequest> findAllByRequester_Id(Long userId);

    @Query("SELECT r " +
            "FROM ParticipationRequest r " +
            "WHERE (r.status LIKE 'CONFIRMED' " +
            "OR r.status like 'REJECTED') " +
            "AND r.event.initiator.id = :userId " +
            "AND r.event.id = :eventId")
    List<ParticipationRequest> findAllConfirmedOrRejected(Long userId, Long eventId);

    @Transactional
    @Query("UPDATE ParticipationRequest r " +
            "SET r.status = :status " +
            "WHERE r.id IN :ids ")
    @Modifying(clearAutomatically = true)
    void updateStatusesByIds(List<Long> ids, ParticipationRequestState status);

    @Transactional
    @Query("UPDATE ParticipationRequest r " +
            "SET r.status = 'REJECTED' " +
            "WHERE r.id = :id")
    @Modifying(clearAutomatically = true)
    void setCanceledById(Long id);

    @Query("UPDATE Event e " +
           "SET e.confirmedRequests = e.confirmedRequests + :value " +
           "WHERE e.id = :eventId")
    @Transactional
    @Modifying(clearAutomatically = true)
    void incrementConfirmedRequestsByEventId(Long eventId, Long value);

    @Query("UPDATE Event e " +
            "SET e.confirmedRequests = e.confirmedRequests - :value " +
            "WHERE e.id = :eventId")
    @Transactional
    @Modifying(clearAutomatically = true)
    void decrementConfirmedRequestsByEventId(Long eventId, Long value);
}
