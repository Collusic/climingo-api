package com.climingo.climingoApi.auth.api;

import com.climingo.climingoApi.auth.api.request.SignInRequest;
import com.climingo.climingoApi.auth.api.response.CheckMemberResponse;
import com.climingo.climingoApi.auth.api.response.TokenResponse;
import com.climingo.climingoApi.auth.application.AuthService;
import com.climingo.climingoApi.auth.application.oauth.OAuth2ClientManager;
import com.climingo.climingoApi.auth.application.oauth.OAuth2UserInfoResponse;
import com.climingo.climingoApi.auth.api.request.SignUpRequest;
import com.climingo.climingoApi.auth.api.response.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OAuth2ClientManager oAuth2ClientManager;

    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponse> signIn(@RequestBody SignInRequest request) {
        OAuth2UserInfoResponse userInfo = oAuth2ClientManager.requestUserInfoFromOAuth2Client(
            request.getProviderType(), request.getProviderToken());
        TokenResponse tokenResponse = authService.signIn(userInfo);

        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<TokenResponse> signUp(@RequestBody final SignUpRequest request) {
        OAuth2UserInfoResponse userInfoFromProvider = oAuth2ClientManager.requestUserInfoFromOAuth2Client(
            request.getProviderType(), request.getProviderToken());
        MemberInfo memberInfo = authService.signUp(request, userInfoFromProvider);
        TokenResponse tokenResponse = authService.issueToken(memberInfo.getNickname());

        return ResponseEntity.ok().body(tokenResponse);
    }

    @GetMapping("/auth/members/exist")
    public ResponseEntity<CheckMemberResponse> checkMemberEnrolled(
        @RequestParam("providerType") String providerType,
        @RequestParam("code") String code,
        @RequestParam("redirectUri") String redirectUri) {

        // provider로 code를 보내 사용자 정보 제공 받기
        OAuth2UserInfoResponse memberInfo = oAuth2ClientManager.requestUserInfoFromOAuth2Client(
            providerType, code, redirectUri);

        // 해당 authId로 가입된 사용자가 있는지 확인
        CheckMemberResponse checkMemberResponse = authService.checkRegisteredMember(memberInfo);

        return ResponseEntity.ok(checkMemberResponse);
    }
}
