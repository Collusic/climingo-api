package com.climingo.climingoApi.security.exception;

import com.climingo.climingoApi.global.exception.ExceptionResponse;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SecurityExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtException.class)
    public ExceptionResponse jwtExceptionHandler(Exception e) {
        return ExceptionResponse.of(HttpStatus.UNAUTHORIZED.getReasonPhrase(), e.getMessage());
    }
}
