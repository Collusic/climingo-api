package com.climingo.climingoApi.auth.api.response;

import com.climingo.climingoApi.member.domain.Member;
import lombok.Getter;

@Getter
public class MemberInfo {

    private final Long id;
    private final String nickname;

    public MemberInfo(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
    }
}
