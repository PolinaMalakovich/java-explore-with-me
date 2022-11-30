package ru.practicum.explorewithme.ewmservice.controller.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.dto.comment.UpdateCommentRequest;
import ru.practicum.explorewithme.ewmservice.service.comment.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events/{eventId}")
@Validated
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment")
    public CommentDto addComment(@PathVariable @PositiveOrZero long eventId,
                                 @Valid @RequestBody NewCommentDto newCommentDto) {
        return commentService.addComment(eventId, newCommentDto);
    }

    @GetMapping("/comments/{commentId}")
    public CommentDto getComment(@PathVariable @PositiveOrZero long eventId,
                                 @PathVariable @PositiveOrZero long commentId) {
        return commentService.getComment(eventId, commentId);
    }

    @GetMapping("/comments")
    public List<CommentDto> getComments(@PathVariable @PositiveOrZero long eventId) {
        return commentService.getComments(eventId);
    }

    @PatchMapping("/comments")
    public CommentDto updateComment(@PathVariable @PositiveOrZero long eventId,
                                    @Valid @RequestBody UpdateCommentRequest updateCommentRequest) {
        return commentService.updateComment(eventId, updateCommentRequest);
    }

    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@PathVariable @PositiveOrZero long eventId,
                              @PathVariable @PositiveOrZero long commentId,
                              @RequestParam @PositiveOrZero long userId) {
        commentService.deleteComment(eventId, commentId, userId);
    }
}
