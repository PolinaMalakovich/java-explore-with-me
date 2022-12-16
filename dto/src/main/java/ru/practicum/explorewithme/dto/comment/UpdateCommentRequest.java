package ru.practicum.explorewithme.dto.comment;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class UpdateCommentRequest {
    long id;
    @NotBlank
    String text;
    long authorId;
}
