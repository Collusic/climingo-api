package com.climingo.climingoApi.auth.exception;

import io.jsonwebtoken.JwtException;

public class ExpiredTokenException extends JwtException {

    public ExpiredTokenException(String message) {
        super(message);
    }
}
