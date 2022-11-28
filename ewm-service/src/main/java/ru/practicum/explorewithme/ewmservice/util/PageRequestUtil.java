package ru.practicum.explorewithme.ewmservice.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;

@UtilityClass
public class PageRequestUtil {
    public static PageRequest getPageRequest(final int from, final int size) {
        return PageRequest.of(from / size, size);
    }
}
