package ru.practicum.explorewithme.ewmservice.spesification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.explorewithme.dto.event.EventState;
import ru.practicum.explorewithme.ewmservice.model.Event;
import ru.practicum.explorewithme.ewmservice.model.ParticipationRequest;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.nonNull;
import static ru.practicum.explorewithme.dto.event.EventState.PUBLISHED;
import static ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestStatus.CONFIRMED;

@UtilityClass
public class EventSpecifications {
    public static Specification<Event> annotationLikeIgnoreCase(final String text) {
        return (event, cq, cb) -> cb.like(cb.lower(event.get("annotation")), cb.literal(text.toLowerCase()));
    }

    public static Specification<Event> descriptionLikeIgnoreCase(final String text) {
        return (event, cq, cb) -> cb.like(cb.lower(event.get("description")), cb.literal(text.toLowerCase()));
    }

    public static Specification<Event> textLikeIgnoreCase(final String text) {
        return annotationLikeIgnoreCase(text).or(descriptionLikeIgnoreCase(text));
    }

    public static Specification<Event> categories(final List<Long> categories) {
        return (event, cq, cb) -> cb.in(event.get("category").get("id")).value(categories);
    }

    public static Specification<Event> users(final List<Long> users) {
        return (event, cq, cb) -> cb.in(event.get("initiator").get("id")).value(users);
    }

    public static Specification<Event> states(final List<EventState> states) {
        return (event, cq, cb) -> cb.in(event.get("state")).value(states);
    }

    public static Specification<Event> onlyAvailable() {
        return (event, cq, cb) -> {
            Subquery<ParticipationRequest> subquery = cq.subquery(ParticipationRequest.class);
            Root<ParticipationRequest> participationRequest = subquery.from(ParticipationRequest.class);
            subquery
                .select(participationRequest)
                .where(
                    cb.equal(participationRequest.get("event").get("id"), event.get("id")),
                    cb.equal(participationRequest.get("status"), CONFIRMED)
                );
            return cb.greaterThan(event.get("participantLimit"), cb.count(participationRequest.get("id")));
        };
    }

//    public static Specification<Event> onlyAvailable() {
//        return (event, cq, cb) -> {
//            Join<Event, ParticipationRequest> participationRequests = event.join("participationRequests");
//            Path<Long> id = participationRequests
//                .on(cb.equal(participationRequests.get("status"), APPROVED))
//                .get("id");
//            return cb.greaterThan(event.get("participantLimit"), cb.count(id));
//        };
//    }

    public static Specification<Event> betweenDates(final LocalDateTime rangeStart, final LocalDateTime rangeEnd) {
        if (nonNull(rangeStart) && nonNull(rangeEnd)) {
            return (event, cq, cb) -> cb.between(event.get("eventDate"), rangeStart, rangeEnd);
        } else {
            return (event, cq, cb) -> cb.greaterThan(event.get("eventDate"), LocalDateTime.now());
        }
    }

    public static Specification<Event> paid(final boolean paid) {
        return (event, cq, cb) -> cb.equal(event.get("paid"), paid);
    }

    public static Specification<Event> published() {
        return (event, cq, cb) -> cb.equal(event.get("state"), PUBLISHED);
    }
}
