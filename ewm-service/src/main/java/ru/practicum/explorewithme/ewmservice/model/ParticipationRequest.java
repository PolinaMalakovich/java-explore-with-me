package ru.practicum.explorewithme.ewmservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "participation_requests")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participation_request_id")
    private Long id;
    @Column(nullable = false)
    private LocalDateTime created = LocalDateTime.now();
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User requester;
    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ParticipationRequestStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipationRequest participationRequest = (ParticipationRequest) o;
        return Objects.equals(id, participationRequest.id)
            && Objects.equals(created, participationRequest.created)
            && Objects.equals(requester, participationRequest.requester)
            && Objects.equals(event, participationRequest.event)
            && Objects.equals(status, participationRequest.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created, requester, event, status);
    }
}
