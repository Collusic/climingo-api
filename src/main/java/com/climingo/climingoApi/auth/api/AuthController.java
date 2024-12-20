package com.climingo.climingoApi.auth.api;

import com.climingo.climingoApi.auth.api.request.SignInRequest;
import com.climingo.climingoApi.auth.api.request.SignUpRequest;
import com.climingo.climingoApi.auth.api.response.CheckMemberResponse;
import com.climingo.climingoApi.auth.api.response.MemberInfo;
import com.climingo.climingoApi.auth.api.response.SignInUpResponse;
import com.climingo.climingoApi.auth.api.response.TokenResponse;
import com.climingo.climingoApi.auth.application.AuthService;
import com.climingo.climingoApi.auth.application.oauth.OAuth2ClientManager;
import com.climingo.climingoApi.auth.application.oauth.OAuth2UserInfoResponse;
import com.climingo.climingoApi.auth.util.CookieUtils;
import com.climingo.climingoApi.auth.util.JwtUtil;
import com.climingo.climingoApi.global.auth.RequestMember;
import com.climingo.climingoApi.member.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;
    private final OAuth2ClientManager oAuth2ClientManager;

    @PostMapping("/sign-in")
    public ResponseEntity<SignInUpResponse> signIn(
        @RequestBody @Valid SignInRequest requestBody,
        HttpServletResponse response) {
        OAuth2UserInfoResponse userInfo = oAuth2ClientManager.requestUserInfoFromOAuth2Client(
            requestBody.getProviderType(), requestBody.getProviderToken());
        TokenResponse tokenResponse = authService.signIn(userInfo);

        CookieUtils.addCookie(response, JwtUtil.ACCESS_TOKEN_NAME, tokenResponse.getAccessToken(),
            JwtUtil.ACCESS_TOKEN_EXP);
        CookieUtils.addCookie(response, JwtUtil.REFRESH_TOKEN_NAME, tokenResponse.getRefreshToken(),
            JwtUtil.REFRESH_TOKEN_EXP);

        MemberInfo memberInfo = authService.findMemberInfo(userInfo);

        return ResponseEntity.ok().body(SignInUpResponse.from(memberInfo));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SignInUpResponse> signUp(
        @RequestBody @Valid final SignUpRequest requestBody,
        HttpServletResponse response) {
        OAuth2UserInfoResponse userInfoFromProvider = oAuth2ClientManager.requestUserInfoFromOAuth2Client(
            requestBody.getProviderType(), requestBody.getProviderToken());
        MemberInfo memberInfo = authService.signUp(requestBody, userInfoFromProvider);
        TokenResponse tokenResponse = authService.issueToken(memberInfo.getMemberId(), memberInfo.getAuthId(),
            memberInfo.getProviderType(), memberInfo.getNickname(), memberInfo.getRole());

        CookieUtils.addCookie(response, "accessToken", tokenResponse.getAccessToken(),
            JwtUtil.ACCESS_TOKEN_EXP);
        CookieUtils.addCookie(response, "refreshToken", tokenResponse.getRefreshToken(),
            JwtUtil.REFRESH_TOKEN_EXP);

        return ResponseEntity.ok().body(SignInUpResponse.from(memberInfo));
    }

    @GetMapping("/auth/members/exist")
    public ResponseEntity<CheckMemberResponse> checkMemberEnrolled(
        @RequestParam("providerType") @Pattern(regexp = "^(kakao|apple)$", message = "providerType은 kakao와 apple만 유효합니다.") String providerType,
        @RequestParam("code") @NotNull String code,
        @RequestParam("redirectUri") @NotNull String redirectUri) {

        // provider로 code를 보내 사용자 정보 제공 받기
        OAuth2UserInfoResponse memberInfo = oAuth2ClientManager.requestUserInfoFromOAuth2Client(
            providerType, code, redirectUri);

        // 해당 authId로 가입된 사용자가 있는지 확인
        CheckMemberResponse checkMemberResponse = authService.checkRegisteredMember(memberInfo);

        return ResponseEntity.ok(checkMemberResponse);
    }

    @DeleteMapping("/sign-out")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, JwtUtil.ACCESS_TOKEN_NAME);
        CookieUtils.deleteCookie(request, response, JwtUtil.REFRESH_TOKEN_NAME);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-member")
    public ResponseEntity<Void> deleteMember(
        @RequestMember Member member, HttpServletRequest request, HttpServletResponse response) {

        authService.deleteMember(member.getId());

        CookieUtils.deleteCookie(request, response, JwtUtil.ACCESS_TOKEN_NAME);
        CookieUtils.deleteCookie(request, response, JwtUtil.REFRESH_TOKEN_NAME);

        return ResponseEntity.ok().build();
    }
}

