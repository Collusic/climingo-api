package com.climingo.climingoApi.member.application;

import com.climingo.climingoApi.member.api.request.SignUpRequest;

public interface SignUpService {

    void signUp(SignUpRequest signUpRequest);

}
