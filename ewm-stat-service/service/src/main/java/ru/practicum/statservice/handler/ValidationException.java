package ru.practicum.statservice.handler;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}