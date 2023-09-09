package ru.practicum.handler;

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
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handlerEntityNotFoundException(EntityNotFoundException e) {
        return new ApiError(
                e.getMessage(),
                "The required object was not found.",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now().format(formatter));
    }

//    @ExceptionHandler(ValidationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ApiError handleValidationException(ValidationException e) {
//        return new ErrorResponse(e.getMessage());
//    }

//    @ExceptionHandler(ConversionFailedException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ApiError handleConversionFailedException(ConversionFailedException e) {
//        return new ErrorResponse("Unknown state: " + e.getValue().toString());
//    }
}

