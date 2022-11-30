package ru.practicum.explorewithme.dto.comment;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Value
public class CommentDto {
    long id;
    @NotBlank
    String text;
    long eventId;
    long authorId;
    String authorName;
    LocalDateTime created;
}
