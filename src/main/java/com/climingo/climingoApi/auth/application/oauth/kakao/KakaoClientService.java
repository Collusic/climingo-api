package com.climingo.climingoApi.auth.application.oauth.kakao;

import com.climingo.climingoApi.auth.application.oauth.OAuth2ClientService;
import com.climingo.climingoApi.auth.application.oauth.OAuth2UserInfoResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Qualifier(value = "KakaoClientService")
@RequiredArgsConstructor
public class KakaoClientService implements OAuth2ClientService {


    private final KakaoTokenApi kakaoTokenApi;

    private final KakaoUserInfoApi kakaoUserInfoApi;

    @Value("${oauth2.kakao.client_id}")
    private String CLIENT_ID;
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String BEARER = "Bearer ";

    @Override
    public OAuth2UserInfoResponse requestAuth(String code, String redirectUri) {
        KakaoTokenUserInfoResponse kakaoTokenResponse = kakaoTokenApi.requestToken(
            CONTENT_TYPE, GRANT_TYPE, CLIENT_ID, redirectUri, code);

        KakaoUserInfoResponse kakaoUserInfoResponse = kakaoUserInfoApi.requestUserInfo(
            CONTENT_TYPE, BEARER + kakaoTokenResponse.getAccessToken());
        return () -> {
            Map<String, Object> attributes = kakaoUserInfoResponse.getAttributes();
            attributes.put("providerToken", kakaoTokenResponse.getAccessToken());
            return attributes;
        };
    }

    @Override
    public OAuth2UserInfoResponse requestAuth(String providerToken) {
        return kakaoUserInfoApi.requestUserInfo(CONTENT_TYPE, BEARER + providerToken);
    }
}
