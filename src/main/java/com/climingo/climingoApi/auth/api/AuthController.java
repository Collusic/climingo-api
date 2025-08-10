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
import feign.Param;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name="AuthController", description = "회원가입, 로그인 관련 api")
@RestController
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;
    private final OAuth2ClientManager oAuth2ClientManager;

    @PostMapping("/sign-in")
    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    @Parameter(name="providerType", description = "provider 타입")
    @Parameter(name="providerToken", description = "provider 유저 정보 조회용 Token")
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
    @Operation(summary="회원가입", description = "회원가입을 진행합니다.")
    @Parameter(name="providerType", description = "provider 타입")
    @Parameter(name="authId", description = "provider에서 제공 받은 유저식별자")
    @Parameter(name="nickname", description = "provider에서 제공 받은 닉네임 혹은 직접 입력")
    @Parameter(name="profileImage", description = "provider에서 제공 받은 profile image url 혹은 직접 입력")
    @Parameter(name="physicalInfo", description = "유저 신체정보(키, 몸무게, 암리치)")
    @Parameter(name="homeGymId", description = "홈 짐 id")

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
    @Operation(summary="회원등록 여부 확인", description = "회원등록을 확인합니다.")
    @Parameter(name="providerType", description = "provider 타입")
    @Parameter(name="code", description = "provider로부터 받은 code")
    @Parameter(name="redirectUri", description = "provider 앱에 등록해둔 redirect uri")
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
    @Operation(summary = "로그아웃", description = "로그아웃을 진행합니다.")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, JwtUtil.ACCESS_TOKEN_NAME);
        CookieUtils.deleteCookie(request, response, JwtUtil.REFRESH_TOKEN_NAME);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-member")
    @Operation(summary="회원 탈퇴", description = "회원 탈퇴를 진행합니다.")
    public ResponseEntity<Void> deleteMember(
        @RequestMember Member member, HttpServletRequest request, HttpServletResponse response) {

        authService.deleteMember(member.getId());

        CookieUtils.deleteCookie(request, response, JwtUtil.ACCESS_TOKEN_NAME);
        CookieUtils.deleteCookie(request, response, JwtUtil.REFRESH_TOKEN_NAME);

        return ResponseEntity.ok().build();
    }
}

