package com.climingo.climingoApi.global.exception;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;

@Getter
@RequiredArgsConstructor
public class ExceptionResponse {

    private final String status;
    private final String message;
    private final List<ExceptionDetailResponse> fieldErrors;

    public static ExceptionResponse of(String status, String message, BindingResult bindingResult) {
        return new ExceptionResponse(status, message, ExceptionDetailResponse.from(bindingResult));
    }

    public static ExceptionResponse of(String status, String message) {
        return new ExceptionResponse(status, message, Collections.emptyList());
    }
}
