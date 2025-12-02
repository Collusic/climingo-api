package com.climingo.climingoApi.jjikboul.application;

import com.climingo.climingoApi.jjikboul.application.response.JjikboulResponse;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.record.api.response.PageDto;

public interface JjikboulQueryUseCase {
    JjikboulResponse readJjikboul(Member member, Long jjikboulId);

    PageDto<JjikboulResponse> readJjikbouls(Member member, Long gymId, Long levelId, Long memberId, Integer page, Integer size);
}