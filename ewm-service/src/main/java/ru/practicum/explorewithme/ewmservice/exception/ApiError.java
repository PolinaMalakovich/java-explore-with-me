package ru.practicum.explorewithme.ewmservice.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {
    List<String> errors;
    String message;
    String reason;
    HttpStatus status;
    LocalDateTime timestamp;
}
