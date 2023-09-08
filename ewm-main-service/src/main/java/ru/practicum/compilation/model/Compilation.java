package ru.practicum.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.event.model.Event;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "compilation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.ALL
    })
    @JoinTable(
            name = "event_compilations",
            joinColumns = {
                    @JoinColumn(name = "compilation_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "event_id")
            }
    )
    private Set<Event> events = new HashSet<>();

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "pinned", nullable = false)
    private Boolean pinned;
}
