package com.climingo.climingoApi.jjikboul.api;

import com.climingo.climingoApi.global.auth.RequestMember;
import com.climingo.climingoApi.jjikboul.api.request.JjikboulCreateRequest;
import com.climingo.climingoApi.jjikboul.api.response.JjikboulCreateResponse;
import com.climingo.climingoApi.jjikboul.application.JjikboulCreateUseCase;
import com.climingo.climingoApi.jjikboul.application.JjikboulQueryUseCase;
import com.climingo.climingoApi.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class JjikboulController {

    private final JjikboulCreateUseCase jjikboulCreateUseCase;

    @PostMapping("/jjikbouls")
    public ResponseEntity<JjikboulCreateResponse> jjikboulsProblem(
            @RequestMember Member member,
            @RequestBody JjikboulCreateRequest request) {

        Long id = jjikboulCreateUseCase.create(member, request);
        
        return ResponseEntity.ok().body(JjikboulCreateResponse.of(id));
    }
}
