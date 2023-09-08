package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.Location;
import ru.practicum.event.enums.EventState;
import ru.practicum.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "annotation", nullable = false)
    private String annotation;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "confirmedRequests")
    private Long confirmedRequests;

    @Column(name = "createdOn")
    private LocalDateTime createdOn;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "description", nullable = false)
    private LocalDateTime eventDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @NotNull
    @Column(name = "paid", nullable = false)
    private Boolean paid;

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
    private String title;

    @Column(name = "views")
    private Long views;
}
