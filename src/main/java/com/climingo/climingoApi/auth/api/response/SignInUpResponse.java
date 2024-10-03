package com.climingo.climingoApi.auth.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInUpResponse {

    private final String nickname;
    private final Long memberId;
    private final String authId;
    private final String providerType;
    private final String email;
    private final String profileUrl;

    @Builder
    public SignInUpResponse(String nickname, Long memberId, String authId, String providerType, String email,
                            String profileUrl) {
        this.nickname = nickname;
        this.memberId = memberId;
        this.authId = authId;
        this.providerType = providerType;
        this.email = email;
        this.profileUrl = profileUrl;
    }

    public static SignInUpResponse from(MemberInfo memberInfo) {
        return SignInUpResponse.builder()
                .nickname(memberInfo.getNickname())
                .memberId(memberInfo.getMemberId())
                .authId(memberInfo.getAuthId())
                .providerType(memberInfo.getProviderType())
                .profileUrl(memberInfo.getProfileUrl())
                .email(memberInfo.getEmail())
                .build();
    }
}
