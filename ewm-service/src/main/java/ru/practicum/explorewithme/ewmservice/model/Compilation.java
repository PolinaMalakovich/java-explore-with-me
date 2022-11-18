package ru.practicum.explorewithme.ewmservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Long id;
    @Column(nullable = false)
    @Size(max = 64, message = "Title cannot be longer than 64 characters.")
    private String title;
    @Column
    private boolean pinned;
    @ManyToMany
    @JoinTable(
        name = "event_compilations",
        joinColumns = {@JoinColumn(name = "compilation_id")},
        inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    private List<Event> events;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compilation compilation = (Compilation) o;
        return Objects.equals(id, compilation.id)
            && Objects.equals(title, compilation.title)
            && Objects.equals(pinned, compilation.pinned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, pinned);
    }
}
