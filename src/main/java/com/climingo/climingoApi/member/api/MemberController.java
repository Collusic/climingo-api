package com.climingo.climingoApi.member.api;

import com.climingo.climingoApi.global.auth.LoginMember;
import com.climingo.climingoApi.member.api.request.UpdateNicknameRequest;
import com.climingo.climingoApi.member.application.MemberService;
import com.climingo.climingoApi.member.api.response.MemberInfoResponse;
import com.climingo.climingoApi.member.api.response.ProfileResponse;
import com.climingo.climingoApi.member.domain.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

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

    @PatchMapping("/members/{memberId}/nickname")
    public ResponseEntity<Void> updateNickname(@LoginMember Member member, @PathVariable(value = "memberId") Long memberId, @RequestBody @Valid UpdateNicknameRequest request) {
        memberService.updateNickname(member, memberId, request.getNickname());
        return ResponseEntity.ok().build();
    }
}