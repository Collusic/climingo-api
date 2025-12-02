package com.climingo.climingoApi.jjikboul.api;

import com.climingo.climingoApi.global.auth.RequestMember;
import com.climingo.climingoApi.jjikboul.api.request.JjikboulCreateRequest;
import com.climingo.climingoApi.jjikboul.api.response.JjikboulCreateResponse;
import com.climingo.climingoApi.jjikboul.application.JjikboulCreateUseCase;
import com.climingo.climingoApi.jjikboul.application.JjikboulDeleteUseCase;
import com.climingo.climingoApi.jjikboul.application.JjikboulQueryUseCase;
import com.climingo.climingoApi.jjikboul.application.response.JjikboulResponse;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.record.api.response.PageDto;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class JjikboulController {

    private final JjikboulCreateUseCase jjikboulCreateUseCase;
    private final JjikboulQueryUseCase jjikboulQueryUseCase;
    private final JjikboulDeleteUseCase jjikboulDeleteUseCase;

    @GetMapping("/jjikbouls")
    public ResponseEntity<PageDto<JjikboulResponse>> getJjikbouls(
            @RequestMember Member member,
            @RequestParam(value = "gymId", required = false) Long gymId,
            @RequestParam(value = "levelId", required = false) Long levelId,
            @RequestParam(value = "memberId", required = false) Long memberId,
            @RequestParam(value = "page", required = false, defaultValue = "0") @Min(0) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Min(1) Integer size
    ) {
        PageDto<JjikboulResponse> jjikboulResponsePageDto = jjikboulQueryUseCase.readJjikbouls(member, gymId, levelId, memberId, page, size);

        return ResponseEntity.ok().body(jjikboulResponsePageDto);
    }

    @GetMapping("/jjikbouls/{jjikboulId}")
    public ResponseEntity<JjikboulResponse> getJjikboul(
            @RequestMember Member member,
            @PathVariable("jjikboulId") Long customProblemId) {
        JjikboulResponse jjikboulResponse = jjikboulQueryUseCase.readJjikboul(member, customProblemId);
        return ResponseEntity.ok().body(jjikboulResponse);
    }

    @PostMapping("/jjikbouls")
    public ResponseEntity<JjikboulCreateResponse> jjikboulsProblem(
            @RequestMember Member member,
            @RequestBody JjikboulCreateRequest request) {

        Long id = jjikboulCreateUseCase.create(member, request);

        return ResponseEntity.ok().body(JjikboulCreateResponse.of(id));
    }

    @DeleteMapping("/jjikbouls/{jjikboulId}")
    public ResponseEntity<JjikboulResponse> deleteJjikboul(
            @RequestMember Member member,
            @PathVariable("jjikboulId") Long customProblemId) {
        jjikboulDeleteUseCase.deleteJjikboul(member, customProblemId);
        return ResponseEntity.ok().build();
    }
}
