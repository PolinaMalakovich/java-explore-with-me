package ru.practicum.explorewithme.ewmservice.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.dto.comment.UpdateCommentRequest;
import ru.practicum.explorewithme.ewmservice.exception.EntityNotFoundException;
import ru.practicum.explorewithme.ewmservice.exception.event.CommentException;
import ru.practicum.explorewithme.ewmservice.model.Comment;
import ru.practicum.explorewithme.ewmservice.model.Event;
import ru.practicum.explorewithme.ewmservice.model.User;
import ru.practicum.explorewithme.ewmservice.repository.CommentRepository;
import ru.practicum.explorewithme.ewmservice.repository.EventRepository;
import ru.practicum.explorewithme.ewmservice.repository.UserRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.practicum.explorewithme.ewmservice.service.comment.CommentMapper.toComment;
import static ru.practicum.explorewithme.ewmservice.service.comment.CommentMapper.toCommentDto;
import static ru.practicum.explorewithme.ewmservice.util.PageRequestUtil.getPageRequest;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CommentDto addComment(final long eventId, final NewCommentDto newCommentDto) {
        final User user = userRepository.findById(newCommentDto.getAuthorId())
            .orElseThrow(() -> new EntityNotFoundException("User", newCommentDto.getAuthorId()));
        final Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event", eventId));
        final Comment comment = commentRepository.save(toComment(newCommentDto, event, user));
        log.info("New comment added successfully.");

        return toCommentDto(comment);
    }

    @Override
    public CommentDto getComment(final long eventId, final long commentId) {
        final Comment comment = commentRepository.findCommentByIdAndEventId(commentId, eventId)
            .orElseThrow(() -> new EntityNotFoundException("Comment", commentId));

        return toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getComments(final long eventId, final int from, final int size) {
        final PageRequest pageable = getPageRequest(from, size);

        return commentRepository
            .findCommentsByEventId(eventId, pageable)
            .map(CommentMapper::toCommentDto)
            .collect(toList());
    }

    @Override
    @Transactional
    public CommentDto updateComment(final long eventId,
                                    final UpdateCommentRequest updateCommentRequest) {
        final Comment comment = commentRepository
            .findCommentByIdAndEventIdAndAuthorId(
                updateCommentRequest.getId(),
                eventId,
                updateCommentRequest.getAuthorId()
            )
            .orElseThrow(() -> new EntityNotFoundException("Comment", updateCommentRequest.getId()));
        int hours = 12;
        if (comment.getCreated().isAfter(comment.getCreated().plusHours(hours))) {
            throw new CommentException("Comments can only be edited within " + hours + " hours after being created.");
        }
        comment.setText(updateCommentRequest.getText());
        log.info("Comment " + comment.getId() + " updated successfully.");

        return toCommentDto(comment);
    }

    @Override
    @Transactional
    public void deleteComment(final long eventId, final long commentId, final long userId) {
        final Comment comment = commentRepository.findCommentByIdAndEventIdAndAuthorId(commentId, eventId, userId)
            .orElseThrow(() -> new EntityNotFoundException("Comment", commentId));
        commentRepository.deleteById(comment.getId());
        log.info("Comment " + comment.getId() + " deleted successfully.");
    }

    @Override
    @Transactional
    public void deleteCommentForAdmin(final long eventId, final long commentId) {
        final Comment comment = commentRepository.findCommentByIdAndEventId(commentId, eventId)
            .orElseThrow(() -> new EntityNotFoundException("Comment", commentId));
        commentRepository.deleteById(comment.getId());
        log.info("Comment " + comment.getId() + " deleted successfully.");
    }
}
