package ru.practicum.explorewithme.ewmservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.ewmservice.exception.event.EventDateException;
import ru.practicum.explorewithme.ewmservice.exception.event.EventException;
import ru.practicum.explorewithme.ewmservice.exception.event.EventModerationException;
import ru.practicum.explorewithme.ewmservice.exception.event.EventStateException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;
import static ru.practicum.explorewithme.util.StringUtils.suffix;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error(e.getMessage());

        return new ApiError(
            List.of("MethodArgumentNotValidException"),
            e.getMessage(),
            "Incorrect request.",
            BAD_REQUEST,
            LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleValidationException(final ValidationException e) {
        log.error(e.getMessage());

        return new ApiError(
            List.of("ValidationException"),
            e.getMessage(),
            "Incorrect request.",
            BAD_REQUEST,
            LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError handleConstraintViolationException(final ConstraintViolationException e) {
        log.error(e.getMessage());
        final List<String> errors = e.getConstraintViolations()
            .stream()
            .map(violation -> suffix(violation.getPropertyPath().toString(), '.'))
            .collect(toList());

        return new ApiError(errors, e.getMessage(), "Constraint violation.", CONFLICT, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleIllegalArgumentException(final IllegalArgumentException e) {
        log.error(e.getMessage());

        return new ApiError(
            List.of("IllegalArgumentException"),
            e.getMessage(),
            "Incorrect request.",
            BAD_REQUEST,
            LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.error(e.getMessage());

        return new ApiError(
            List.of("DataIntegrityViolationException"),
            e.getMessage(),
            "Incorrect request.",
            CONFLICT,
            LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.error(e.getMessage());

        return new ApiError(
            List.of("MissingServletRequestParameterException"),
            e.getMessage(),
            "Incorrect request.",
            BAD_REQUEST,
            LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleInvalidDataAccessApiUsageException(final InvalidDataAccessApiUsageException e) {
        log.error(e.getMessage());

        return new ApiError(
            List.of("InvalidDataAccessApiUsageException"),
            e.getMessage(),
            "Incorrect request.",
            BAD_REQUEST,
            LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ApiError handleEntityNotFoundException(final EntityNotFoundException e) {
        log.error(e.getMessage());

        return new ApiError(
            List.of("EntityNotFoundException"),
            e.getMessage(),
            "Incorrect request.",
            NOT_FOUND,
            LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError handleConflictException(final ConflictException e) {
        log.error(e.getMessage());

        return new ApiError(
            List.of("ConflictException"),
            e.getMessage(),
            "Incorrect request.",
            CONFLICT,
            LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleEventDateException(final EventDateException e) {
        log.error(e.getMessage());

        return new ApiError(
            List.of("EventDateException"),
            e.getMessage(),
            "Incorrect request.",
            BAD_REQUEST,
            LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleEventStateException(final EventStateException e) {
        log.error(e.getMessage());

        return new ApiError(
            List.of("EventStateException"),
            e.getMessage(),
            "Incorrect request.",
            BAD_REQUEST,
            LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(FORBIDDEN)
    public ApiError handleForbiddenException(final ForbiddenException e) {
        log.error(e.getMessage());

        return new ApiError(
            List.of("ForbiddenException"),
            e.getMessage(),
            "Incorrect request.",
            FORBIDDEN,
            LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handlePermissionDeniedException(final PermissionDeniedException e) {
        log.error(e.getMessage());

        return new ApiError(
            List.of("PermissionDeniedException"),
            e.getMessage(),
            "Incorrect request.",
            BAD_REQUEST,
            LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleEventModerationException(final EventModerationException e) {
        log.error(e.getMessage());

        return new ApiError(
            List.of("EventModerationException"),
            e.getMessage(),
            "Incorrect request.",
            BAD_REQUEST,
            LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleEventException(final EventException e) {
        log.error(e.getMessage());

        return new ApiError(
            List.of("EventException"),
            e.getMessage(),
            "Incorrect request.",
            BAD_REQUEST,
            LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        log.error(e.getMessage());

        return new ApiError(
            List.of("Throwable"),
            e.getMessage(),
            "Server error.",
            INTERNAL_SERVER_ERROR,
            LocalDateTime.now()
        );
    }
}
