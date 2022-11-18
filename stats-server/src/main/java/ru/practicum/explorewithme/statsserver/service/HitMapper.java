package ru.practicum.explorewithme.statsserver.service;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.hit.HitDto;
import ru.practicum.explorewithme.statsserver.model.Hit;

@UtilityClass
public class HitMapper {
    public static Hit toHit(final HitDto hitDto) {
        return new Hit(
            hitDto.getId(),
            hitDto.getApp(),
            hitDto.getUri(),
            hitDto.getIp(),
            hitDto.getTimestamp()
        );
    }

    public static HitDto toHitDto(final Hit hit) {
        return new HitDto(hit.getId(), hit.getApp(), hit.getUri(), hit.getIp(), hit.getTimestamp());
    }
}
