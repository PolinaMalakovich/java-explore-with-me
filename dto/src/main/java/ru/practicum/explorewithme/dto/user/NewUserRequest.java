package ru.practicum.explorewithme.dto.user;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
public class NewUserRequest {
    @NotBlank
    String name;
    @NotBlank
    @Email
    String email;
}
