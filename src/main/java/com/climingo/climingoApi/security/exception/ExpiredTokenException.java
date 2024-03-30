package com.climingo.climingoApi.security.exception;

import io.jsonwebtoken.JwtException;

public class ExpiredTokenException extends JwtException {

    public ExpiredTokenException(String message) {
        super(message);
    }
}
