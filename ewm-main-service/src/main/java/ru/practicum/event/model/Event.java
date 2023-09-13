package ru.practicum.event.model;

import lombok.*;
import ru.practicum.category.model.Category;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.enums.EventState;
import ru.practicum.location.model.Location;
import ru.practicum.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @NotEmpty
    @NotBlank
    @Column(name = "annotation", nullable = false)
    private String annotation;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

//    @Column(name = "confirmed_requests")
    private Long confirmedRequests;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "event_date", nullable = false)
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

    @Column(name = "participant_limit")
    private Long participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private EventState state;

    @NotEmpty
    @NotBlank
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "views")
    private Long views;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "events", cascade = { CascadeType.ALL })
    private Set<Compilation> compilations = new HashSet<>();

    public Event(String annotation,
                 Category category,
                 String description,
                 LocalDateTime eventDate,
                 User initiator,
                 Location location,
                 Boolean paid,
                 String title) {
        this.annotation = annotation;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.location = location;
        this.paid = paid;
        this.title = title;
    }
}
