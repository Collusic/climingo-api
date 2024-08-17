package com.climingo.climingoApi.auth.application;

import com.climingo.climingoApi.auth.api.request.SignUpRequest;
import com.climingo.climingoApi.auth.api.response.MemberInfo;

public interface MemberEnrollService {

    MemberInfo enroll(SignUpRequest signUpRequest);

    MemberInfo findEnrolledMemberInfoByAuthIdAndProviderType(String authId, String providerType);
}
