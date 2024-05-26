package com.climingo.climingoApi.global.exception;

import jakarta.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse validationExceptionHandler(MethodArgumentNotValidException e) {
        log.error("[error]", e);
        return ExceptionResponse.of(HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage(), e.getBindingResult());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ExceptionResponse missingRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        log.error("[error]", e);
        return ExceptionResponse.of(HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ExceptionResponse constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.error("[error]", e);
        return ExceptionResponse.of(HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ExceptionResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ExceptionResponse.of(HttpStatus.CONFLICT.getReasonPhrase(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ExceptionResponse handleAccessDeniedException(AccessDeniedException e) {
        return ExceptionResponse.of(HttpStatus.FORBIDDEN.getReasonPhrase(), e.getMessage());
    }
}
