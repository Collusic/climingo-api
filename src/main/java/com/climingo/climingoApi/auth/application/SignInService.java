package com.climingo.climingoApi.auth.application;

import com.climingo.climingoApi.auth.api.response.MemberInfo;

public interface SignInService {


    MemberInfo findEnrolledMemberInfoByAuthIdAndProviderType(String authId, String providerType);
}
