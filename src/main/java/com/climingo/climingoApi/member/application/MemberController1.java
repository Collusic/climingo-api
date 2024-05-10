package com.climingo.climingoApi.member.application;

import com.climingo.climingoApi.member.application.response.MemberInfoResponse;
import com.climingo.climingoApi.member.application.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController1 {

    private final MemberService1 memberService;

    @GetMapping("/members")
    public ResponseEntity<ProfileResponse> findMyInfo() {
        ProfileResponse profileResponse = memberService.findMyInfo(1L);
        return ResponseEntity.ok().body(profileResponse);
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<MemberInfoResponse> findMemberInfo(@PathVariable(value = "memberId") Long memberId) {
        MemberInfoResponse memberInfoResponse = memberService.findMemberInfo(memberId);
        return ResponseEntity.ok().body(memberInfoResponse);
    }

}
