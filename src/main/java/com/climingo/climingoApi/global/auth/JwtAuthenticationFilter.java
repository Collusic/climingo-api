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

            CustomUserDetails userDetails = verify(accessToken);

            authenticationToken = new JWTAuthenticationToken(userDetails,
                Set.of(new SimpleGrantedAuthority("USER")));
            authenticationToken.setAuthenticated(true);
        } catch (RuntimeException e) {
            log.debug("while authenticate accessToken: ", e);

            authenticationToken = new JWTAuthenticationToken(null, Set.of(new SimpleGrantedAuthority("GUEST")));
            authenticationToken.setAuthenticated(false);
        }

        return authenticationToken;
    }

    private CustomUserDetails verify(String token) {
        JwtUtil.verify(token);

        Map<String, Object> claims = JwtUtil.getClaims(token);
        Long memberId = ((Integer) claims.get("memberId")).longValue();
        String authId = (String) claims.get("authId");
        String providerType = (String) claims.get("providerType");
        String nickname = (String) claims.get("nickname");
        String role = (String) claims.get("role");

        return new CustomUserDetails(memberId, authId, providerType, nickname, role);
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
            CustomUserDetails userDetails = verify(refreshToken);

            TokenResponse tokenResponse = authTokenService.issue(
                userDetails.getMemberId(),
                userDetails.getAuthId(),
                userDetails.getProviderType(),
                userDetails.getNickname(), userDetails.getRole());

            authTokenService.update(userDetails.getMemberId(), tokenResponse);

            CookieUtils.addCookie(response, JwtUtil.ACCESS_TOKEN_NAME,
                tokenResponse.getAccessToken(), JwtUtil.ACCESS_TOKEN_EXP);
            CookieUtils.addCookie(response, JwtUtil.REFRESH_TOKEN_NAME,
                tokenResponse.getRefreshToken(), JwtUtil.REFRESH_TOKEN_EXP);

            authenticationToken = new JWTAuthenticationToken(
                userDetails, Set.of(new SimpleGrantedAuthority(userDetails.getRole())));
            authenticationToken.setAuthenticated(true);

        } catch (JwtException e) {
            log.debug("while authenticate refreshToken: ", e);

            Optional<Cookie> refreshTokenCookie = CookieUtils.getCookie(request,
                JwtUtil.REFRESH_TOKEN_NAME);

            String refreshToken = refreshTokenCookie
                .orElse(CookieUtils.genreateEmptyCookie("refreshToken"))
                .getValue();

            if (refreshToken != null) {
                authTokenService.deleteByRefreshToken(refreshToken);
            }

            authenticationToken = new JWTAuthenticationToken(
                CustomUserDetails.createGuest(), Set.of(new SimpleGrantedAuthority("GUEST")));
            authenticationToken.setAuthenticated(false);
        } catch (RuntimeException e) {
            log.debug("while authenticate refreshToken: ", e);

            authenticationToken = new JWTAuthenticationToken(
                CustomUserDetails.createGuest(), Set.of(new SimpleGrantedAuthority("GUEST")));
            authenticationToken.setAuthenticated(false);
        }

        return authenticationToken;
    }
}
