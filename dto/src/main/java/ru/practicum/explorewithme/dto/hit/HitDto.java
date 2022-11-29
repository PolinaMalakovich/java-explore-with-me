package ru.practicum.explorewithme.dto.hit;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class HitDto {
    Long id;
    String app;
    String uri;
    String ip;
    LocalDateTime timestamp;
}
