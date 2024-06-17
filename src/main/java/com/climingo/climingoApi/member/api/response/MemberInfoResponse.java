package com.climingo.climingoApi.member.api.response;

import com.climingo.climingoApi.member.domain.Member;
import lombok.Getter;

@Getter
public class MemberInfoResponse {

    private Long memberId;
    private String nickname;
    private String profileUrl;
    private String providerType;

    public MemberInfoResponse(Member member) {
        this.memberId = member.getId();
        this.nickname = member.getNickname();
        this.profileUrl = member.getProfileUrl();
        this.providerType = member.getProviderType();
    }

}
