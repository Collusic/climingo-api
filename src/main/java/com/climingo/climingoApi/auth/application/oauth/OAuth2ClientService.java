package com.climingo.climingoApi.auth.application.oauth;

public interface OAuth2ClientService {

    OAuth2UserInfoResponse requestAuth(String code, String redirectUri);

    OAuth2UserInfoResponse requestAuth(String providerToken);
}
