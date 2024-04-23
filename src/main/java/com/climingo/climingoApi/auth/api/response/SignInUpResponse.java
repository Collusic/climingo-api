package com.climingo.climingoApi.auth.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInUpResponse {

    private final String nickname;
    private final String authId;
    private final String providerType;
    private final String email;
    private final String picture;

    @Builder
    public SignInUpResponse(String nickname, String authId, String providerType, String email,
        String picture) {
        this.nickname = nickname;
        this.authId = authId;
        this.providerType = providerType;
        this.email = email;
        this.picture = picture;
    }
}