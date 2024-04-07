package com.climingo.climingoApi.global.exception;

import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NoSuchElementException.class, IllegalArgumentException.class})
    public ExceptionResponse badRequestExceptionHandler(Exception e) {
        log.error("[error]", e);
        return ExceptionResponse.of(HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
    }
}
