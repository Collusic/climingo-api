package com.climingo.climingoApi.auth.application.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2ClientManager {

    @Qualifier("kakaoClientService")
    private final OAuth2ClientService kakaoClientService;

    public OAuth2UserInfoResponse requestUserInfoFromOAuth2Client(String provider, String code, String redirectUri) {
        OAuth2ClientService target = getService(provider);
        return target.requestAuth(code, redirectUri);
    }

    private OAuth2ClientService getService(String provider) {
        // TODO 애플 로그인 추가 시 구현
        return kakaoClientService;
    }

    public OAuth2UserInfoResponse requestUserInfoFromOAuth2Client(String provider, String providerToken) {
        OAuth2ClientService target = getService(provider);
        return target.requestAuth(providerToken);
    }
}
