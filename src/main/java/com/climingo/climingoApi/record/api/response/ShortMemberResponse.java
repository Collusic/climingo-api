package com.climingo.climingoApi.record.api.response;

import lombok.Getter;

@Getter
public class ShortMemberResponse {

    private Long memberId;

    private String profileImage;

    private String nickname;

    public ShortMemberResponse(Long memberId, String profileImage, String nickname) {
        this.memberId = memberId;
        this.profileImage = profileImage;
        this.nickname = nickname;
    }

}
