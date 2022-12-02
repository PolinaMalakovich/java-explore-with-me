package ru.practicum.explorewithme.ewmservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.ewmservice.model.Comment;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findCommentByIdAndAuthorId(long commentId, long authorId);

    Optional<Comment> findCommentByIdAndEventId(long commentId, long eventId);

    Optional<Comment> findCommentByIdAndEventIdAndAuthorId(long commentId, long eventId, long authorId);

    Stream<Comment> findCommentsByEventId(long eventId, Pageable pageable);
}
