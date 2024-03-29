package com.climingo.climingoApi.member.application;

import com.climingo.climingoApi.member.api.request.SignUpRequest;
import com.climingo.climingoApi.member.domain.Member;

public interface SignUpService {

    Member signUp(SignUpRequest signUpRequest);

}
