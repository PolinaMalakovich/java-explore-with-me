package ru.practicum.explorewithme.ewmservice.service.comment;

import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.dto.comment.UpdateCommentRequest;

import java.util.List;

public interface CommentService {
    CommentDto addComment(long eventId, NewCommentDto newCommentDto);

    CommentDto getComment(long eventId, long commentId);

    List<CommentDto> getComments(long eventId, int from, int size);

    CommentDto updateComment(long eventId, UpdateCommentRequest updateCommentRequest);

    void deleteComment(long eventId, long commentId, long userId);

    void deleteCommentForAdmin(long eventId, long commentId);
}
