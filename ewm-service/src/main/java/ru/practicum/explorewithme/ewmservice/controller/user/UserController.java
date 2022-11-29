package ru.practicum.explorewithme.ewmservice.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.ewmservice.service.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam final List<Long> ids,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                  @RequestParam(defaultValue = "10") @Positive final int size) {
        return userService.getUsers(ids, from, size);
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody final NewUserRequest newUserRequest) {
        return userService.addUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @PositiveOrZero final long userId) {
        userService.deleteUser(userId);
    }
}
