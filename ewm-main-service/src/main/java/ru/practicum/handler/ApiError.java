package ru.practicum.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ApiError {
    //private List<String> errors;
    private String message;
    private String reason;
    private HttpStatus status;
    private String timestamp;
}
