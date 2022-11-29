package ru.practicum.explorewithme.ewmservice.repository.participationrequest;

import ru.practicum.explorewithme.ewmservice.model.Event;

import java.util.List;
import java.util.Map;

public interface CustomParticipationRequestRepository {
    Map<Long, Long> getConfirmedRequests(List<Event> eventList);
}
