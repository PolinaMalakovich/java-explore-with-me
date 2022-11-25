package ru.practicum.explorewithme.ewmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.ewmservice.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
