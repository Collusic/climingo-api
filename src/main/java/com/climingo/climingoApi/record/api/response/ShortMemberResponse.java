package com.climingo.climingoApi.record.api.response;

import lombok.Getter;

@Getter
public class ShortMemberResponse {

    private Long memberId;

    private String profileUrl;

    private String nickname;

    public ShortMemberResponse(Long memberId, String profileUrl, String nickname) {
        this.memberId = memberId;
        this.profileUrl = profileUrl;
        this.nickname = nickname;
    }

}
