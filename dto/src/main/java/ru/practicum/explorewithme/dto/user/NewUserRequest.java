package ru.practicum.explorewithme.dto.user;

import lombok.Value;

@Value
public class NewUserRequest {
    String name;
    String email;
}
