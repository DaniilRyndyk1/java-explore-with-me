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

    @ExceptionHandler(RequestFailedMeetConditionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleRequestFailedMeetConditionException(RequestFailedMeetConditionException e) {
        return new ApiError(
                e.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.FORBIDDEN,
                getTimestamp());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConversionFailedException(ConflictException e) {
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

    private String getTimestamp() {
        return LocalDateTime.now().format(formatter);
    }
}

