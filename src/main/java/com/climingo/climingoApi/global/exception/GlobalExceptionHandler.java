package com.climingo.climingoApi.global.exception;

import com.climingo.climingoApi.message.error.ErrorAlertMessageProvider;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import javax.naming.AuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorAlertMessageProvider messageProvider;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public ExceptionResponse badRequestExceptionHandler(Exception e) {
        log.error("[BAD_REQUEST]", e);
        return ExceptionResponse.of(HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AuthenticationException.class})
    public ExceptionResponse unAuthorizedExceptionHandler(AuthenticationException e) {
        log.error("[BAD_REQUEST]", e);
        return ExceptionResponse.of(HttpStatus.UNAUTHORIZED.getReasonPhrase(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse validationExceptionHandler(MethodArgumentNotValidException e) {
        log.error("[BAD_REQUEST]", e);
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
        log.error("[BAD_REQUEST]", e);
        return ExceptionResponse.of(HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
    public ExceptionResponse notFoundExceptionHandler(Exception e) {
        log.error("[NOT_FOUND]", e);
        return ExceptionResponse.of(HttpStatus.NOT_FOUND.getReasonPhrase(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ExceptionResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ExceptionResponse.of(HttpStatus.CONFLICT.getReasonPhrase(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = {AccessDeniedException.class, ForbiddenException.class})
    public ExceptionResponse handleAccessDeniedException(RuntimeException e) {
        return ExceptionResponse.of(HttpStatus.FORBIDDEN.getReasonPhrase(), e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ExceptionResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ExceptionResponse.of(HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public ExceptionResponse handleNoResourceFoundException(Exception e) {
        return ExceptionResponse.of(HttpStatus.NOT_FOUND.getReasonPhrase(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ExceptionResponse handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        try {
            messageProvider.sendMessage(e, request);
        } catch (Exception ex) {
            log.error("[ERROR] 디스코드 웹훅 에러");
            ex.printStackTrace();
        }
        return ExceptionResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
    }
}
