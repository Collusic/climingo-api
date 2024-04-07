package com.climingo.climingoApi.auth.application;

import com.climingo.climingoApi.auth.api.request.SignUpRequest;
import com.climingo.climingoApi.auth.api.response.CheckMemberResponse;
import com.climingo.climingoApi.auth.api.response.MemberInfo;
import com.climingo.climingoApi.auth.api.response.TokenResponse;
import com.climingo.climingoApi.auth.application.oauth.OAuth2UserInfoResponse;
import com.climingo.climingoApi.member.domain.MemberRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenService tokenService;
    private final SignUpService signUpService;
    private final MemberRepository memberRepository;

    private final static String AUTHID_KEY = "authId";
    private final static String PROVIDER_KEY = "providerType";

    public CheckMemberResponse checkRegisteredMember(OAuth2UserInfoResponse memberInfo) {
        Map<String, Object> attributes = memberInfo.getAttributes();
        boolean registered = checkExistMember(
            attributes.get(AUTHID_KEY).toString(),
            attributes.get(PROVIDER_KEY).toString());

        return new CheckMemberResponse(registered, attributes.get("providerToken").toString());
    }

    public TokenResponse signIn(OAuth2UserInfoResponse memberInfo) {
        Map<String, Object> attributes = memberInfo.getAttributes();
        String authId = (String) attributes.get(AUTHID_KEY);
        String providerType = (String) attributes.get(PROVIDER_KEY);

        if (!checkExistMember(authId, providerType)) {
            throw new RuntimeException("등록되지 않은 사용자. 회원가임을 먼저 진행하세요.");
        }

        return tokenService.issue((String) memberInfo.getAttributes().get("nickname"));
    }

    private boolean checkExistMember(String authId, String providerType) {
        return memberRepository.existsByAuthIdAndProviderType(authId, providerType);
    }

    public MemberInfo signUp(SignUpRequest signUpRequest,
        OAuth2UserInfoResponse userInfoFromProvider) {
        Map<String, Object> attributes = userInfoFromProvider.getAttributes();
        String authId = (String) attributes.get(AUTHID_KEY);
        String providerType = (String) attributes.get(PROVIDER_KEY);

        validateSignUp(signUpRequest, authId, providerType);

        return signUpService.signUp(signUpRequest);
    }

    private void validateSignUp(SignUpRequest signUpRequest, String authId, String providerType) {
        if (!isEqualsToInput(signUpRequest, authId, providerType)) {
            throw new RuntimeException("입력받은 정보와 provider로부터 받은 값이 일치하지 않습니다.");
        }

        if (checkExistMember(authId, providerType)) {
            throw new RuntimeException("이미 존재하는 회원입니다.");
        }
    }

    private boolean isEqualsToInput(SignUpRequest signUpRequest, String authId,
        String providerType) {
        return signUpRequest.getProviderType().equals(providerType) &&
            signUpRequest.getAuthId().equals(authId);
    }

    public TokenResponse issueToken(String nickname) {
        return tokenService.issue(nickname);
    }
}
