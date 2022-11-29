package ru.practicum.explorewithme.ewmservice.service.user;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;
import ru.practicum.explorewithme.ewmservice.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {
    public static UserDto toUserDto(final User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User toUser(final UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    public static UserShortDto toUserShortDto(final User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

    public static User toUser(final UserShortDto userShortDto, final String email) {
        return new User(userShortDto.getId(), userShortDto.getName(), email);
    }

    public static User toUser(final NewUserRequest newUserRequest) {
        return new User(null, newUserRequest.getName(), newUserRequest.getEmail());
    }

    public static List<UserDto> toUserDtoList(List<User> users) {
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
