package com.climingo.climingoApi.auth.application;

import com.climingo.climingoApi.auth.api.response.CheckMemberResponse;
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
    private final MemberRepository memberRepository;

    private final static String AUTHID_KEY = "authId";
    private final static String PROVIDER_KEY = "provider";

    public CheckMemberResponse checkRegisteredMember(OAuth2UserInfoResponse memberInfo) {
        Map<String, Object> attributes = memberInfo.getAttributes();
        boolean registered = checkExistMember(
            attributes.get(AUTHID_KEY).toString(),
            attributes.get(PROVIDER_KEY).toString());

        return new CheckMemberResponse(registered, attributes.get("providerToken").toString());
    }

    public TokenResponse authenticateAndIssueToken(OAuth2UserInfoResponse memberInfo) {
        Map<String, Object> attributes = memberInfo.getAttributes();
        String authId = (String) attributes.get(AUTHID_KEY);
        String provider = (String) attributes.get(PROVIDER_KEY);

        if (!checkExistMember(authId, provider)) {
            throw new RuntimeException("등록되지 않은 사용자. 회원가임을 먼저 진행하세요.");
        }

        return tokenService.issue((String) memberInfo.getAttributes().get("nickname"));
    }

    private boolean checkExistMember(String authId, String provider) {
        return memberRepository.existsByAuthIdAndProviderType(authId, provider);
    }
}
