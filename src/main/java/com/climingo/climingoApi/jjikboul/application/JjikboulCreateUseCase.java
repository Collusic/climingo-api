package com.climingo.climingoApi.jjikboul.application;

import com.climingo.climingoApi.jjikboul.api.request.JjikboulCreateRequest;
import com.climingo.climingoApi.member.domain.Member;

public interface JjikboulCreateUseCase {
    Long create(Member member, JjikboulCreateRequest request);
}
