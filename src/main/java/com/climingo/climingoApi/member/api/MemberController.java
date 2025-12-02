package com.climingo.climingoApi.member.api;

import com.climingo.climingoApi.global.auth.RequestMember;
import com.climingo.climingoApi.member.api.request.UpdateNicknameRequest;
import com.climingo.climingoApi.member.api.response.MemberInfoResponse;
import com.climingo.climingoApi.member.application.MemberService;
import com.climingo.climingoApi.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="MemberController", description = "회원 정보 조회 및 수정 api")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    @Operation(summary = "내 정보 조회", description = "사용자의 정보를 조회합니다.")
    @Parameter(name = "memberId", description = "member id 를 토큰으로 부터 추출함")
    public ResponseEntity<MemberInfoResponse> findMyInfo(@RequestMember Member loginMember) {
        MemberInfoResponse memberInfoResponse = memberService.findMemberInfo(loginMember.getId());
        return ResponseEntity.ok().body(memberInfoResponse);
    }

    @GetMapping("/members/{memberId}")
    @Operation(summary = "멤버 정보 상세 조회", description = "사용자 정보 상세 조회 합니다.")
    @Parameter(name = "memberId", description = "멤버 아이디")
    public ResponseEntity<MemberInfoResponse> findMemberInfo(@PathVariable(value = "memberId") Long memberId) {
        MemberInfoResponse memberInfoResponse = memberService.findMemberInfo(memberId);
        return ResponseEntity.ok().body(memberInfoResponse);
    }

    @PatchMapping("/members/{memberId}/nickname")
    @Operation(summary = "멤버 닉네임 변경", description = "회원정보 닉네임 변경")
    @Parameter(name = "nickname", description = "변경할 닉네임")
    public ResponseEntity<Void> updateNickname(@RequestMember Member member, @PathVariable(value = "memberId") Long memberId,
                                               @RequestBody @Valid UpdateNicknameRequest request) {
        memberService.updateNickname(member, memberId, request.getNickname());
        return ResponseEntity.ok().build();
    }

}
