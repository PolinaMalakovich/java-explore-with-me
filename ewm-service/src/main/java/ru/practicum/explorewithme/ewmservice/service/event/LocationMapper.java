package ru.practicum.explorewithme.ewmservice.service.event;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.event.LocationDto;
import ru.practicum.explorewithme.ewmservice.model.Location;

@UtilityClass
public class LocationMapper {
    public static LocationDto toLocationDto(final Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }

    public static Location toLocation(final LocationDto locationDto) {
        return new Location(locationDto.getLat(), locationDto.getLon());
    }
}
