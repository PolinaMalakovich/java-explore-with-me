package ru.practicum.explorewithme.ewmservice.exception;

import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Value
public class ApiError {
    List<String> errors;
    String message;
    String reason;
    HttpStatus status;
    LocalDateTime timestamp;
}
