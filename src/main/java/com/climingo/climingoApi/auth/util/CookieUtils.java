package com.climingo.climingoApi.auth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Optional;

// [reference] https://velog.io/@cutepassions/spring-security-%EC%84%A4%EC%A0%95-3-cookie
public class CookieUtils {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(name)).findAny();
    }

    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);

        String origin = request.getHeader("Origin");
        String domain = null;

        if (origin != null) {
            try {
                URI originUri = new URI(origin);
                domain = originUri.getHost();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        if (domain != null && !domain.equals("localhost")) {
            cookie.setDomain(domain);
        }

        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
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
}
