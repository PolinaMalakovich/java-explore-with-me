package ru.practicum.explorewithme.ewmservice.service.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.ewmservice.model.Comment;
import ru.practicum.explorewithme.ewmservice.model.Event;
import ru.practicum.explorewithme.ewmservice.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public static CommentDto toCommentDto(final Comment comment) {
        return new CommentDto(
            comment.getId(),
            comment.getText(),
            comment.getEvent().getId(),
            comment.getAuthor().getId(),
            comment.getAuthor().getName(),
            comment.getCreated()
        );
    }

    public static Comment toComment(final CommentDto commentDto, final Event event, final User user) {
        return new Comment(
            commentDto.getId(),
            commentDto.getText(),
            event,
            user,
            commentDto.getCreated()
        );
    }

    public static Comment toComment(final NewCommentDto newCommentDto, final Event event, final User user) {
        return new Comment(
            null,
            newCommentDto.getText(),
            event,
            user,
            LocalDateTime.now()
        );
    }
}
