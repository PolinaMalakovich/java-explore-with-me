package ru.practicum.explorewithme.ewmservice.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.ewmservice.exception.ConflictException;
import ru.practicum.explorewithme.ewmservice.model.User;
import ru.practicum.explorewithme.ewmservice.repository.UserRepository;

import java.util.List;

import static ru.practicum.explorewithme.ewmservice.service.user.UserMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(final List<Long> userIds, final int from, final int size) {
        if (userIds.isEmpty()) {
            List<User> users = userRepository.findAll(PageRequest.of(from / size, size)).toList();
            return toUserDtoList(users);
        } else {
            return toUserDtoList(userRepository.findAllById(userIds));
        }
    }

    @Override
    @Transactional
    public UserDto addUser(final NewUserRequest newUserRequest) {
        final String name = newUserRequest.getName();
        User nameLike = userRepository.findByNameLike(name);
        if (nameLike != null) {
            throw new ConflictException("User", name);
        }
        final User user = toUser(newUserRequest);
        final User newUser = userRepository.save(user);
        log.info("New user created successfully.");

        return toUserDto(newUser);
    }

    @Override
    @Transactional
    public void deleteUser(final long userId) {
        userRepository.deleteById(userId);
        log.info("User " + userId + " deleted successfully.");
    }
}
