package com.climingo.climingoApi.auth.api.response;

import com.climingo.climingoApi.member.domain.Member;
import lombok.Getter;

@Getter
public class MemberInfo {

    private final String nickname;
    private final Long memberId;
    private final String role;
    private final String authId;
    private final String providerType;
    private final String email;
    private final String profileUrl;

    public MemberInfo(Member member) {
        this.nickname = member.getNickname();
        this.memberId = member.getId();
        this.role = member.getRole().toString();
        this.authId = member.getAuthId();
        this.providerType = member.getProviderType();
        this.email = member.getEmail();
        this.profileUrl = member.getProfileUrl();
    }
}
