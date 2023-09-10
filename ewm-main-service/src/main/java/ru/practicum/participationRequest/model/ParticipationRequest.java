package ru.practicum.participationRequest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.event.model.Event;
import ru.practicum.participationRequest.enums.ParticipationRequestState;
import ru.practicum.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "request")
@Getter
@Setter
@AllArgsConstructor
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ParticipationRequestState status;
}
