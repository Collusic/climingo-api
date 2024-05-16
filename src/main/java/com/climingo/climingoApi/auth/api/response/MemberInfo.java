package com.climingo.climingoApi.auth.api.response;

import com.climingo.climingoApi.member.domain.Member;
import lombok.Getter;

@Getter
public class MemberInfo {

    private final String nickname;
    private final String authId;
    private final String providerType;
    private final String email;
    private final String picture;

    public MemberInfo(Member member) {
        this.nickname = member.getNickname();
        this.authId = member.getAuthId();
        this.providerType = member.getProviderType();
        this.email = member.getEmail();
        this.picture= member.getProfileUrl();
    }
}
