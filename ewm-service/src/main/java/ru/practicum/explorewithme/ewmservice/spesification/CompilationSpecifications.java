package ru.practicum.explorewithme.ewmservice.spesification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.explorewithme.ewmservice.model.Compilation;

@UtilityClass
public class CompilationSpecifications {
    public static Specification<Compilation> pinned(final boolean pinned) {
        return (event, cq, cb) -> cb.equal(event.get("pinned"), pinned);
    }
}
