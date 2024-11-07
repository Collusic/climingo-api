package com.climingo.climingoApi.auth.application.oauth.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "KakaoProfileClient", url = "https://kapi.kakao.com")
public interface KakaoUserInfoApi {

    @GetMapping(value = "/v1/oidc/userinfo", consumes = "application/x-www-form-urlencoded;charset=utf-8")
    KakaoUserInfoResponse requestUserInfo(
        @RequestHeader("Content-type") String contentType,
        @RequestHeader("Authorization") String accessToken
    );
}