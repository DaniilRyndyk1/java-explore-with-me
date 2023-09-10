package ru.practicum.participationRequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.participationRequest.enums.ParticipationRequestState;
import ru.practicum.participationRequest.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    ParticipationRequest findFirstByRequester_IdAndEvent_Id(Long requesterId, Long eventId);
    List<ParticipationRequest> findAllByRequester_Id(Long requesterId);

    @Query("SELECT r FROM ParticipationRequest r WHERE r.status like 'CONFIRMED' OR r.status like 'REJECTED'")
    List<ParticipationRequest> findAllConfirmedOrRejected();

    @Query("UPDATE ParticipationRequest r SET r.status = :status WHERE r.id IN :ids")
    void updateStatusesByIds(List<Long> ids, ParticipationRequestState status);

    @Query("UPDATE ParticipationRequest r SET r.status = 'CANCELED' WHERE r.id = :id")
    void setCanceledById(Long id);
}
