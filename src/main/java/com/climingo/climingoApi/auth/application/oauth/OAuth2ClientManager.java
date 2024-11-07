package com.climingo.climingoApi.auth.application.oauth;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class OAuth2ClientManager {

    private final OAuth2ClientService kakaoClientService;
    private final OAuth2ClientService appleClientService;

    public OAuth2ClientManager(
        @Qualifier(value = "KakaoClientService") OAuth2ClientService kakaoClientService,
        @Qualifier(value = "AppleClientService") OAuth2ClientService appleClientService) {
        this.kakaoClientService = kakaoClientService;
        this.appleClientService = appleClientService;
    }

    public OAuth2UserInfoResponse requestUserInfoFromOAuth2Client(String provider, String code,
        String redirectUri) {
        OAuth2ClientService target = getService(provider);
        return target.requestAuth(code, redirectUri);
    }

    private OAuth2ClientService getService(String provider) {
        if (provider.equals("kakao")) {
            return kakaoClientService;
        }

        return appleClientService;
    }

    public OAuth2UserInfoResponse requestUserInfoFromOAuth2Client(String provider,
        String providerToken) {
        OAuth2ClientService target = getService(provider);
        return target.requestAuth(providerToken);
    }
}
