package com.climingo.climingoApi.security.util;

import com.climingo.climingoApi.security.exception.ExpiredTokenException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtil {

    public static final int REFRESH_TOKEN_EXP = 60 * 60 * 24 * 7;
    public static final String KEY = "climingo-token-key";
    private static final int ACCESS_TOKEN_EXP = 60 * 60;

    public static String createAccessToken(String nickname) {
        return Jwts.builder()
            .setHeader(jwtHeaders())
            .claim("nickname", nickname)
            .claim("exp", Instant.now().getEpochSecond() + ACCESS_TOKEN_EXP)
            .signWith(SignatureAlgorithm.HS256, KEY)
            .compact();
    }

    public static Claims verify(String token) {
        try {
            return Jwts.parser()
                .setSigningKey(KEY)
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            log.info("토큰 만료");
            throw new ExpiredTokenException("토큰 만료");
        } catch (UnsupportedJwtException e) {
            log.info("애플리케이션에서 지원하지 않는 토큰 형식");
            throw new UnsupportedJwtException("애플리케이션에서 지원하지 않는 토큰 형식");
        } catch (MalformedJwtException e) {
            log.info("구조적인 문제가 있는 토큰");
            throw new MalformedJwtException("구조적인 문제가 있는 토큰");
        } catch (SignatureException e) {
            log.info("시그니처 검증 실패");
            throw new SignatureException("시그니처 검증 실패");
        } catch (IllegalArgumentException e) {
            log.info("부적합한 인수");
            throw new IllegalArgumentException("부적합한 인수");
        }
    }

    public static String createRefreshToken(String nickname) {
        return Jwts.builder()
            .setHeader(jwtHeaders())
            .claim("nickname", nickname)
            .claim("exp", Instant.now().getEpochSecond() + REFRESH_TOKEN_EXP)
            .signWith(SignatureAlgorithm.HS256, KEY)
            .compact();
    }

    public static Map<String, Object> getClaims(String token) {
        Base64.Decoder decoder = Base64.getDecoder();
        String payload = new String(decoder.decode(token.split("\\.")[1]));

        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> claims = mapper.readValue(payload, Map.class);
            return claims;
        } catch (JsonProcessingException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    public static String getNickName(String token) {
        return (String) getClaims(token).get("email");
    }

    private static Map<String, Object> jwtHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", SignatureAlgorithm.HS256);
        return headers;
    }
}
