package ru.practicum.explorewithme.dto.user;

import lombok.Value;

@Value
public class UserDto {
    Long id;
    String name;
    String email;
}
