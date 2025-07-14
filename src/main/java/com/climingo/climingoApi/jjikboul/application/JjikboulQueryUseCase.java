package com.climingo.climingoApi.jjikboul.application;

import com.climingo.climingoApi.jjikboul.application.response.JjikboulResponse;
import com.climingo.climingoApi.member.domain.Member;

public interface JjikboulQueryUseCase {
    JjikboulResponse readJjikboul(Member member, Long jjikboulId);
}
