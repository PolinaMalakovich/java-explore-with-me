package ru.practicum.explorewithme.ewmservice.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.ewmservice.model.Compilation;

import java.util.stream.Stream;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Stream<Compilation> findByPinned(boolean pinned, PageRequest pageRequest);
}
