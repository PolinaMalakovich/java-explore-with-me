package ru.practicum.explorewithme.statsserver.service;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.hit.HitDto;
import ru.practicum.explorewithme.statsserver.model.App;
import ru.practicum.explorewithme.statsserver.model.Hit;

@UtilityClass
public class HitMapper {
    public static Hit toHit(final HitDto hitDto, final App app) {
        return new Hit(
            hitDto.getId(),
            app,
            hitDto.getUri(),
            hitDto.getIp(),
            hitDto.getTimestamp()
        );
    }

    public static HitDto toHitDto(final Hit hit) {
        return new HitDto(hit.getId(), hit.getApp().getName(), hit.getUri(), hit.getIp(), hit.getTimestamp());
    }
}
