package ru.practicum.event.model;

import lombok.*;
import ru.practicum.category.model.Category;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.enums.EventState;
import ru.practicum.location.model.Location;
import ru.practicum.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
@Getter
@Setter
@RequiredArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "annotation", nullable = false)
    private final String annotation;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private final Category category;

    @Column(name = "confirmedRequests")
    private Long confirmedRequests;

    @Column(name = "createdOn")
    private LocalDateTime createdOn;

    @Column(name = "description")
    private final String description;

    @NotNull
    @Column(name = "eventDate", nullable = false)
    private final LocalDateTime eventDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private final User initiator;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "location_id")
    private final Location location;

    @NotNull
    @Column(name = "paid", nullable = false)
    private final Boolean paid;

    @Column(name = "participantLimit")
    private Integer participantLimit;

    @Column(name = "publishedOn")
    private LocalDateTime publishedOn;

    @Column(name = "requestModeration")
    private Boolean requestModeration;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private EventState state;

    @NotNull
    @Column(name = "title", nullable = false)
    private final String title;

    @Column(name = "views")
    private Long views;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "events", cascade = { CascadeType.ALL })
    private Set<Compilation> compilations = new HashSet<>();
}
