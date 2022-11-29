package ru.practicum.explorewithme.dto.hit;

import lombok.Value;

@Value
public class Stats {
    String app;
    String uri;
    long hits;
}
