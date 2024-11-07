package com.climingo.climingoApi.auth.application.oauth.apple;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "AppleValidateTokenClient", url = "https://appleid.apple.com")
public interface AppleValidateTokenApi {

    @PostMapping(value = "/auth/oauth2/v2/token", consumes = "application/x-www-form-urlencoded")
    AppleValidateTokenResponse validateToken(
        @RequestParam("client_id") String clientId,
        @RequestParam("grant_type") String grantType,
        @RequestParam("client_secret") String clientSecret,
        @RequestParam("refresh_token") String refreshToken);
}