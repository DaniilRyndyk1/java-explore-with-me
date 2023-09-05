package ru.practicum.statservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statservice.dto.EndpointHitResultDto;
import ru.practicum.statservice.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.statservice.dto.EndpointHitResultDto(hit.app, hit.uri, count(hit.id)) " +
            "FROM EndpointHit hit " +
            "WHERE hit.uri IN :uris AND hit.created BETWEEN :start AND :end " +
            "GROUP BY hit.app, hit.uri")
    List<EndpointHitResultDto> findByPeriodAndUriIn(LocalDateTime start,
                                                    LocalDateTime end,
                                                    String[] uris);

    @Query("SELECT new ru.practicum.statservice.dto.EndpointHitResultDto(hit.app, hit.uri, count(hit.id)) " +
            "FROM EndpointHit hit " +
            "WHERE hit.created BETWEEN :start AND :end " +
            "GROUP BY hit.app, hit.uri")
    List<EndpointHitResultDto> findByPeriod(LocalDateTime start,
                                            LocalDateTime end);

    @Query("SELECT new ru.practicum.statservice.dto.EndpointHitResultDto(hit.app, hit.uri, count(DISTINCT hit.ip)) " +
            "FROM EndpointHit hit " +
            "WHERE hit.uri IN :uris AND hit.created BETWEEN :start AND :end " +
            "GROUP BY hit.app, hit.uri")
    List<EndpointHitResultDto> findUniqueByPeriodAndUriIn(LocalDateTime start,
                                                          LocalDateTime end,
                                                          String[] uris);

    @Query("SELECT new ru.practicum.statservice.dto.EndpointHitResultDto(hit.app, hit.uri, count(DISTINCT hit.ip)) " +
            "FROM EndpointHit hit " +
            "WHERE hit.created BETWEEN :start AND :end " +
            "GROUP BY hit.app, hit.uri")
    List<EndpointHitResultDto> findUniqueByPeriod(LocalDateTime start,
                                                  LocalDateTime end);
}
