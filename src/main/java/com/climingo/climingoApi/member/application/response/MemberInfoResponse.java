package com.climingo.climingoApi.member.application.response;

import com.climingo.climingoApi.member.domain.Member;
import lombok.Getter;

@Getter
public class MemberInfoResponse {

    private Long memberId;
    private String nickname;
    private String profileImage;

    public MemberInfoResponse(Member member) {
        this.memberId = member.getId();
        this.nickname = member.getNickname();
        this.profileImage = member.getProfileImage();
    }

}
