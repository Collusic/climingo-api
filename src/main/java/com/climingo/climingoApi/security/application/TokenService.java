package com.climingo.climingoApi.security.application;

import com.climingo.climingoApi.security.api.response.TokenResponse;
import com.climingo.climingoApi.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    public TokenResponse issue(String nickname) {
        String accessToken = JwtUtil.createAccessToken(nickname);
        String refreshToken = JwtUtil.createRefreshToken(nickname);

        // TODO 로그아웃을 위한 토큰 영속화 기능

        return new TokenResponse(accessToken, refreshToken);
    }
}
