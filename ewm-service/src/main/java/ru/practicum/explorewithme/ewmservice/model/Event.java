package ru.practicum.explorewithme.ewmservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.dto.event.EventState;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    @Column
    @Size(min = 3, max = 120, message = "Title should be shorter than 120 and longer than 3 characters.")
    private String title;
    @Column
    @Size(min = 20, max = 2000, message = "Annotation should be shorter than 2000 and longer than 20 characters.")
    private String annotation;
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column
    @Size(min = 20, max = 7000, message = "Description should be shorter than 7000 and longer than 20 characters.")
    private String description;
    @Column(nullable = false)
    private LocalDateTime created = LocalDateTime.now();
    @Column
    private LocalDateTime published;
    @Column(nullable = false)
    private LocalDateTime eventDate;
    @Embedded
    private Location location;
    @Column
    private boolean paid;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User initiator;
    @Column
    private int participantLimit;
    @Column
    private boolean requestModeration;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id)
            && Objects.equals(title, event.title)
            && Objects.equals(annotation, event.annotation)
            && Objects.equals(category, event.category)
            && Objects.equals(description, event.description)
            && Objects.equals(created, event.created)
            && Objects.equals(published, event.published)
            && Objects.equals(eventDate, event.eventDate)
            && Objects.equals(location, event.location)
            && Objects.equals(paid, event.paid)
            && Objects.equals(initiator, event.initiator)
            && Objects.equals(participantLimit, event.participantLimit)
            && Objects.equals(requestModeration, event.requestModeration)
            && Objects.equals(state, event.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, annotation, category, description, created, published, eventDate, location, paid,
            initiator, participantLimit, requestModeration, state);
    }
}
