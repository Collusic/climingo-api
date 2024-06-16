package com.climingo.climingoApi.member.application;

import com.climingo.climingoApi.member.api.response.MemberInfoResponse;
import com.climingo.climingoApi.member.domain.Member;

public interface MemberService {

    MemberInfoResponse findMemberInfo(Long memberId);

    void updateNickname(Member member, Long memberId, String nickname);

}
