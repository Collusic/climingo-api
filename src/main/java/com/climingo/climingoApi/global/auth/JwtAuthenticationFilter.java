package com.climingo.climingoApi.global.auth;

import com.climingo.climingoApi.auth.api.response.TokenResponse;
import com.climingo.climingoApi.auth.application.AuthTokenService;
import com.climingo.climingoApi.auth.util.CookieUtils;
import com.climingo.climingoApi.auth.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final AuthTokenService authTokenService;

    public JwtAuthenticationFilter(
        AuthenticationManager authenticationManager, AuthTokenService authTokenService) {
        super(authenticationManager);
        this.authTokenService = authTokenService;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain chain
    ) throws IOException, ServletException {
        authenticateWithToken(request, response);
        chain.doFilter(request, response);
    }

    private void authenticateWithToken(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authenticateWithAccessToken(request);
        if (!authentication.isAuthenticated()) {
            authentication = authenticateWithRefreshToken(request, response);
        }

        if (!authentication.isAuthenticated()) {
            SecurityContextHolder.clearContext();
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication authenticateWithAccessToken(HttpServletRequest request) {
        JWTAuthenticationToken authenticationToken;

        try {
            Optional<Cookie> accessTokenCookie = CookieUtils.getCookie(request,
                JwtUtil.ACCESS_TOKEN_NAME);

            String accessToken = accessTokenCookie
                .orElse(CookieUtils.genreateEmptyCookie("accessToken"))
                .getValue();

            authTokenService.checkLoginedAccessToken(accessToken);
            JwtUtil.verify(accessToken);

            // TODO authentication 절차 추후 리팩토링 예정
            authenticationToken = new JWTAuthenticationToken(accessToken,
                Set.of(new SimpleGrantedAuthority("ROLE_USER")));
//        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            authenticationToken.setAuthenticated(true);
        } catch (RuntimeException e) {
            log.debug("while authenticate accessToken: ", e);

            authenticationToken = new JWTAuthenticationToken(null, Set.of(new SimpleGrantedAuthority("ROLE_USER")));
//        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            authenticationToken.setAuthenticated(false);
        }

        return authenticationToken;
    }

    private Authentication authenticateWithRefreshToken(HttpServletRequest request,
        HttpServletResponse response) {
        JWTAuthenticationToken authenticationToken;

        try {
            Optional<Cookie> refreshTokenCookie = CookieUtils.getCookie(request,
                JwtUtil.REFRESH_TOKEN_NAME);

            String refreshToken = refreshTokenCookie
                .orElse(CookieUtils.genreateEmptyCookie("refreshToken"))
                .getValue();

            authTokenService.checkLoginedRefreshToken(refreshToken);
            JwtUtil.verify(refreshToken);

            Map<String, Object> claims = JwtUtil.getClaims(refreshToken);
            Long memberId = ((Integer) claims.get("memberId")).longValue();
            String authId = (String) claims.get("authId");
            String providerType = (String) claims.get("providerType");
            String nickname = (String) claims.get("nickname");

            TokenResponse tokenResponse = authTokenService.issue(memberId, authId, providerType, nickname);
            authTokenService.update(memberId, tokenResponse);

            CookieUtils.addCookie(request, response, JwtUtil.ACCESS_TOKEN_NAME,
                tokenResponse.getAccessToken(), JwtUtil.ACCESS_TOKEN_EXP);
            CookieUtils.addCookie(request, response, JwtUtil.REFRESH_TOKEN_NAME,
                tokenResponse.getRefreshToken(), JwtUtil.REFRESH_TOKEN_EXP);

            // TODO authentication 절차 추후 리팩토링 예정
            authenticationToken = new JWTAuthenticationToken(
                tokenResponse.getAccessToken(), Set.of(new SimpleGrantedAuthority("ROLE_USER")));
//        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            authenticationToken.setAuthenticated(true);

        } catch (JwtException e) {
            log.debug("while authenticate refreshToken: ", e);

            Optional<Cookie> refreshTokenCookie = CookieUtils.getCookie(request,
                JwtUtil.REFRESH_TOKEN_NAME);

            String refreshToken = refreshTokenCookie
                .orElse(CookieUtils.genreateEmptyCookie("refreshToken"))
                .getValue();

            if (refreshToken != null) {
                authTokenService.delete(refreshToken);
            }

            // TODO authentication 절차 추후 리팩토링 예정
            authenticationToken = new JWTAuthenticationToken(
                null, Set.of(new SimpleGrantedAuthority("ROLE_USER")));
//        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            authenticationToken.setAuthenticated(false);
        } catch (RuntimeException e) {
            log.debug("while authenticate refreshToken: ", e);

            // TODO authentication 절차 추후 리팩토링 예정
            authenticationToken = new JWTAuthenticationToken(
                null, Set.of(new SimpleGrantedAuthority("ROLE_USER")));
//        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            authenticationToken.setAuthenticated(false);
        }

        return authenticationToken;
    }
}
