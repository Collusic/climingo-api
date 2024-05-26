package com.climingo.climingoApi.member.application;

import com.climingo.climingoApi.member.api.response.MemberInfoResponse;
import com.climingo.climingoApi.member.api.response.ProfileResponse;
import com.climingo.climingoApi.member.domain.Member;

public interface MemberService {

    ProfileResponse findMyInfo(Long memberId);

    MemberInfoResponse findMemberInfo(Long memberId);

    void updateNickname(Member member, Long memberId, String nickname);
}
