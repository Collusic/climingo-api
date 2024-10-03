package com.climingo.climingoApi.auth.application.oauth.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "KakaoAccessTokenClient", url = "https://kauth.kakao.com")
public interface KakaoTokenApi {

    @PostMapping(value = "/oauth/token", consumes = "application/json")
    KakaoTokenUserInfoResponse requestToken(
        @RequestHeader("Content-Type") String contentType,
        @RequestParam("grant_type") String grantType,
        @RequestParam("client_id") String clientId,
        @RequestParam("redirect_uri") String redirectUri,
        @RequestParam("code") String code
    );
}