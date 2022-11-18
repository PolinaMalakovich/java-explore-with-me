package ru.practicum.explorewithme.statsserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.dto.hit.Stats;
import ru.practicum.explorewithme.statsserver.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long>, JpaSpecificationExecutor<Hit> {

    @Query(nativeQuery = true, name = "Hit.getAllUnique")
    List<Stats> getAllUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(nativeQuery = true, name = "Hit.getAll")
    List<Stats> getAll(LocalDateTime start, LocalDateTime end, List<String> uris);
}
