package com.climingo.climingoApi.jjikboul.application;

import com.climingo.climingoApi.member.domain.Member;

public interface JjikboulDeleteUseCase {
    void deleteJjikboul(Member requestMember, Long customProblemId);
}
