package com.climingo.climingoApi.member.application;

import com.climingo.climingoApi.member.api.request.SignUpRequest;
import com.climingo.climingoApi.member.api.response.MemberInfo;

public interface SignUpService {

    MemberInfo signUp(SignUpRequest signUpRequest);

}
