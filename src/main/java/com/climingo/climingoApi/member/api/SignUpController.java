package com.climingo.climingoApi.member.api;

import com.climingo.climingoApi.member.api.request.SignUpRequest;
import com.climingo.climingoApi.member.application.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody final SignUpRequest signUpRequest) {

        signUpService.signUp(signUpRequest);

        return ResponseEntity.ok().build();
    }
}
