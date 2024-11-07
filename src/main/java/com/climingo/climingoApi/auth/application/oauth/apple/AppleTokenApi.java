package com.climingo.climingoApi.auth.application.oauth.apple;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "AppleAccessTokenClient", url = "https://appleid.apple.com")
public interface AppleTokenApi {

    @PostMapping(value = "/auth/oauth2/v2/token", consumes = "application/json")
    AppleTokenInfoResponse requestToken(
        @RequestParam("content_type") String contentType,
        @RequestParam("grant_type") String grantType,
        @RequestParam("client_secret") String clientSecret,
        @RequestParam("client_id") String clientId,
        @RequestParam("redirect_uri") String redirectUri,
        @RequestParam("code") String code
    );
}
