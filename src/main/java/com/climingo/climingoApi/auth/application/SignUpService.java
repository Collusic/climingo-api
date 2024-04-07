package com.climingo.climingoApi.auth.application;

import com.climingo.climingoApi.auth.api.request.SignUpRequest;
import com.climingo.climingoApi.auth.api.response.MemberInfo;

public interface SignUpService {

    MemberInfo signUp(SignUpRequest signUpRequest);

}
