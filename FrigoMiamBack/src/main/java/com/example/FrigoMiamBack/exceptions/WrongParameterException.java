package com.example.FrigoMiamBack.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class WrongParameterException extends RuntimeException {
    private String message;
    private HttpStatus status;
    private LocalDateTime time;

    public WrongParameterException(String message, HttpStatus status, LocalDateTime time) {
        super(message);
        this.message = message;
        this.status = status;
        this.time = time;
    }
}
