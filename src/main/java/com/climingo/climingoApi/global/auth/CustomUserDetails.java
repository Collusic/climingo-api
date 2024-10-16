package com.climingo.climingoApi.global.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomUserDetails {

    private final Long memberId;

    private final String authId;

    private final String providerType;

    private final String nickname;

    private final String role;

    public static Object createGuest() {
        return new CustomUserDetails(null, null, null, null, "GUEST");
    }
}
