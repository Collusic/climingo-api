package com.climingo.climingoApi.auth.application;

import com.climingo.climingoApi.auth.api.response.TokenResponse;
import com.climingo.climingoApi.auth.domain.AuthToken;
import com.climingo.climingoApi.auth.domain.AuthTokenRepository;
import com.climingo.climingoApi.auth.exception.ExpiredTokenException;
import com.climingo.climingoApi.auth.util.JwtUtil;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final AuthTokenRepository authTokenRepository;

    @Transactional
    public TokenResponse issue(Long memberId, String authId, String providerType, String nickname, String role) {
        String accessToken = JwtUtil.createToken(memberId, authId, providerType, nickname, role, false);
        String refreshToken = JwtUtil.createToken(memberId, authId, providerType, nickname, role, true);

        Optional<AuthToken> tokenInfo = authTokenRepository.findById(memberId);
        AuthToken authToken = AuthToken.of(memberId, accessToken, refreshToken);

        if (tokenInfo.isPresent()) {
            authToken = tokenInfo.get();
            authToken.update(accessToken, refreshToken);
        }

        authTokenRepository.save(authToken);

        return new TokenResponse(accessToken, refreshToken);
    }

    public void checkLoginedAccessToken(String accessToken) {
        if (!isExistByAccessToken(accessToken)) {
            throw new ExpiredTokenException("만료된 토큰");
        }
    }

    private boolean isExistByAccessToken(String accessToken) {
        return authTokenRepository.existsByAccessToken(accessToken);
    }

    @Transactional
    public void update(Long memberId, TokenResponse tokenResponse) {
        var token = authTokenRepository.findById(memberId)
            .orElseThrow(NoSuchElementException::new); // todo

        token.update(tokenResponse);
        authTokenRepository.save(token);
    }

    public void deleteByRefreshToken(String refreshToken) {
        authTokenRepository.deleteByRefreshToken(refreshToken);
    }

    public void checkLoginedRefreshToken(String refreshToken) {
        if (!isExistByRefreshToken(refreshToken)) {
            throw new ExpiredTokenException("만료된 토큰");
        }
    }

    private boolean isExistByRefreshToken(String refreshToken) {
        return authTokenRepository.existsByRefreshToken(refreshToken);
    }

    public void deleteByMemberId(Long memberId) {
        authTokenRepository.deleteById(memberId);
    }
}
