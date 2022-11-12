package ru.practicum.explorewithme.ewmservice.service.user;

import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> userIds, int from, int size);

    UserDto addUser(NewUserRequest newUserRequest);

    void deleteUser(long userId);
}
