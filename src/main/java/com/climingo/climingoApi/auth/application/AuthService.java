package com.climingo.climingoApi.auth.application;

import com.climingo.climingoApi.auth.api.request.SignUpRequest;
import com.climingo.climingoApi.auth.api.response.CheckMemberResponse;
import com.climingo.climingoApi.auth.api.response.MemberInfo;
import com.climingo.climingoApi.auth.api.response.TokenResponse;
import com.climingo.climingoApi.auth.application.oauth.OAuth2UserInfoResponse;
import com.climingo.climingoApi.member.domain.MemberRepository;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenService tokenService;
    private final SignUpService signUpService;
    private final SignInService signInService;
    private final MemberRepository memberRepository;

    private final static String AUTHID_KEY = "authId";
    private final static String PROVIDER_KEY = "providerType";

    public CheckMemberResponse checkRegisteredMember(OAuth2UserInfoResponse memberInfo) {
        Map<String, Object> attributes = memberInfo.getAttributes();
        String authId = attributes.get(AUTHID_KEY).toString();
        String providerType = attributes.get(PROVIDER_KEY).toString();
        boolean registered = checkExistMember(
            authId,
            providerType);

        return new CheckMemberResponse(registered, attributes);
    }

    public TokenResponse signIn(OAuth2UserInfoResponse memberInfo) {
        Map<String, Object> attributes = memberInfo.getAttributes();
        String authId = (String) attributes.get(AUTHID_KEY);
        String providerType = (String) attributes.get(PROVIDER_KEY);
        String nickname = (String) memberInfo.getAttributes().get("nickname");

        if (!checkExistMember(authId, providerType)) {
            throw new NoSuchElementException("등록되지 않은 사용자. 회원가입을 먼저 진행하세요.");
        }


        return tokenService.issue(authId, providerType, nickname);
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
            throw new IllegalArgumentException("입력받은 정보와 provider로부터 받은 값이 일치하지 않습니다.");
        }
    }

    private boolean isEqualsToInput(SignUpRequest signUpRequest, String authId,
        String providerType) {
        return signUpRequest.getProviderType().equals(providerType) &&
            signUpRequest.getAuthId().equals(authId);
    }

    public TokenResponse issueToken(String authId, String providerType, String nickname) {
        return tokenService.issue(authId, providerType, nickname);
    }

    public MemberInfo findMemberInfo(OAuth2UserInfoResponse userInfo) {
        Map<String, Object> attributes = userInfo.getAttributes();
        String authId = (String) attributes.get(AUTHID_KEY);
        String providerType = (String) attributes.get(PROVIDER_KEY);

        return signInService.findEnrolledMemberInfoByAuthIdAndProviderType(authId, providerType);
    }
}
