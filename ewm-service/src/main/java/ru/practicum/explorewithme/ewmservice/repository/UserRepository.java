package ru.practicum.explorewithme.ewmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.ewmservice.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByNameLike(String name);
}
