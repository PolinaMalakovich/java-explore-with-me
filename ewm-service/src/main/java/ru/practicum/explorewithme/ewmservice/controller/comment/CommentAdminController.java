package ru.practicum.explorewithme.ewmservice.controller.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.ewmservice.service.comment.CommentService;

import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@Validated
public class CommentAdminController {
    private final CommentService commentService;

    @DeleteMapping("/admin/events/{eventId}/comments/{commentId}")
    public void deleteComment(@PathVariable @PositiveOrZero long eventId,
                              @PathVariable @PositiveOrZero long commentId) {
        commentService.deleteCommentForAdmin(eventId, commentId);
    }
}
