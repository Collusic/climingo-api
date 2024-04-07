package com.climingo.climingoApi.auth.api;

import com.climingo.climingoApi.auth.api.request.SignInRequest;
import com.climingo.climingoApi.auth.api.response.CheckMemberResponse;
import com.climingo.climingoApi.auth.api.response.TokenResponse;
import com.climingo.climingoApi.auth.application.AuthService;
import com.climingo.climingoApi.auth.application.oauth.OAuth2ClientManager;
import com.climingo.climingoApi.auth.application.oauth.OAuth2UserInfoResponse;
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
            request.getProvider(), request.getProviderToken());
        TokenResponse tokenResponse = authService.authenticateAndIssueToken(userInfo);

        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/auth/members/exist")
    public ResponseEntity<CheckMemberResponse> checkMemberEnrolled(
        @RequestParam("provider") String provider,
        @RequestParam("code") String code,
        @RequestParam("redirectUri") String redirectUri) {

        // provider로 code를 보내 사용자 정보 제공 받기
        OAuth2UserInfoResponse memberInfo = oAuth2ClientManager.requestUserInfoFromOAuth2Client(
            provider, code, redirectUri);

        // 해당 authId로 가입된 사용자가 있는지 확인
        CheckMemberResponse checkMemberResponse = authService.checkRegisteredMember(memberInfo);

        return ResponseEntity.ok(checkMemberResponse);
    }
}
