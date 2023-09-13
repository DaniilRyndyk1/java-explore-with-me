package ru.practicum.handler;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ErrorHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handlerNotFoundException(NotFoundException e) {
        return new ApiError(
                e.getMessage(),
                "The required object was not found.",
                HttpStatus.NOT_FOUND,
                getTimestamp());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handlerEntityNotFoundException(EntityNotFoundException e) {
        return new ApiError(
                e.getMessage(),
                "The required object was not found.",
                HttpStatus.NOT_FOUND,
                getTimestamp());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(ConflictException e) {
        return new ApiError(
                e.getMessage(),
                "Integrity constraint has been violated.",
                HttpStatus.CONFLICT,
                getTimestamp());
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataAccessException(DataAccessException e) {
        return new ApiError(
                e.getMessage(),
                "Integrity constraint has been violated.",
                HttpStatus.CONFLICT,
                getTimestamp());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(ValidationException e) {
        return new ApiError(
                e.getMessage(),
                "Incorrectly made request.",
                HttpStatus.BAD_REQUEST,
                getTimestamp());
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleUnsupportedOperationException(UnsupportedOperationException e) {
        return new ApiError(
                e.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.FORBIDDEN,
                getTimestamp());
    }

    private String getTimestamp() {
        return LocalDateTime.now().format(formatter);
    }
}

