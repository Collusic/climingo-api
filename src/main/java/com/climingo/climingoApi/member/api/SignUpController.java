package com.climingo.climingoApi.member.api;

import com.climingo.climingoApi.member.api.request.SignUpRequest;
import com.climingo.climingoApi.member.api.response.MemberInfo;
import com.climingo.climingoApi.member.application.SignUpService;
import com.climingo.climingoApi.security.api.response.TokenResponse;
import com.climingo.climingoApi.security.application.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;
    private final TokenService tokenService;

    @PostMapping("/sign-up")
    public ResponseEntity<TokenResponse> signUp(@RequestBody final SignUpRequest signUpRequest) {

        // 트랜잭션 기준을 어케 잡을까
        MemberInfo memberInfo = signUpService.signUp(signUpRequest);
        TokenResponse tokenResponse = tokenService.issue(memberInfo.getNickname());

        return ResponseEntity.ok().body(tokenResponse);
    }
}
