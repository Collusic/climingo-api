package com.climingo.climingoApi.auth.exception;

import com.climingo.climingoApi.global.exception.ExceptionResponse;
import feign.FeignException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtException.class)
    public ExceptionResponse jwtExceptionHandler(Exception e) {
        log.error("[error] ", e);
        return ExceptionResponse.of(HttpStatus.UNAUTHORIZED.getReasonPhrase(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FeignException.class)
    public ExceptionResponse feignExceptionHandler(FeignException e) {
        log.error("[error] ", e);
        return ExceptionResponse.of(HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
    }
}
