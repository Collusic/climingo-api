package com.climingo.climingoApi.auth.util;

import com.google.common.net.HttpHeaders;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;

// [reference] https://velog.io/@cutepassions/spring-security-%EC%84%A4%EC%A0%95-3-cookie
public class CookieUtils {

    @Value("${auth.cookie.domain}")
    private static String COOKIE_DOMAIN;

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(name)).findAny();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {

        ResponseCookie cookie = ResponseCookie.from(name, value)
            .domain(COOKIE_DOMAIN)
            .maxAge(maxAge)
            .sameSite("None")
            .path("/")
            .httpOnly(true)
            .secure(true)
            .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static Cookie genreateEmptyCookie(String name) {
        return new Cookie(name, "TEMP");
    }
}
