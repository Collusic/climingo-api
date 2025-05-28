package com.climingo.climingoApi.record.api;

import com.climingo.climingoApi.global.auth.RequestMember;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.record.api.request.RecordCreateRequest;
import com.climingo.climingoApi.record.api.request.RecordUpdateRequest;
import com.climingo.climingoApi.record.api.response.MyRecordResponse;
import com.climingo.climingoApi.record.api.response.PageDto;
import com.climingo.climingoApi.record.api.response.RecordResponse;
import com.climingo.climingoApi.record.application.RecordService;
import com.climingo.climingoApi.record.domain.Record;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;

import java.io.IOException;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="RecordController", description = "영상 기록 api")
@RequiredArgsConstructor
@RestController
public class RecordController {

    private final RecordService recordService;

    @PostMapping("/records")
    @Operation(summary = "기록 생성", description = "사용자가 기록을 작성합니다.")
    @Parameter(name = "gymId", description = "클라이밍장 id")
    @Parameter(name = "leveId", description = "난이도 id")
    @Parameter(name = "videoUrl", description = "영상 url")
    public ResponseEntity<Map<String, Long>> create(
            @RequestMember Member member,
            @RequestBody RecordCreateRequest request) throws IOException {

        Record record = recordService.createRecord(member, request);
        return ResponseEntity.ok().body(Map.of("recordId", record.getId()));
    }

    @PatchMapping("/records/{recordId}")
    @Operation(summary = "기록 수정", description = "게시된 기록 수정합니다.(추후 수정할 필드가 생기면 추가예정)")
    @Parameter(name = "gymId", description = "클라이밍장 id")
    @Parameter(name = "levelId", description = "난이도 id")
    @Parameter(name = "video", description = "영상")
    public ResponseEntity<Long> update(
            @RequestMember Member member,
            @PathVariable("recordId") Long recordId,
            @ModelAttribute RecordUpdateRequest request) {

        Record record = recordService.updateRecord(member, recordId, request);
        return ResponseEntity.ok().body(record.getId());
    }

    @DeleteMapping("/records/{recordId}")
    @Operation(summary = "기록 삭제", description = "기록을 삭제합니다.")
    @Parameter(name = "recordId", description = "기록(게시글) Id")
    public ResponseEntity<Void> delete(
            @RequestMember Member member,
            @PathVariable("recordId") Long recordId) {
        recordService.deleteRecord(member, recordId);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/records/{recordId}")
    @Operation(summary = "기록 상세 조회", description = "1건의 기록을 조회합니다.")
    @Parameter(name = "recordId", description = "클라이밍장 id")
    public ResponseEntity<RecordResponse> find(
            @RequestMember Member member,
            @PathVariable("recordId") Long recordId) {
        RecordResponse recordResponse = recordService.readRecord(member, recordId);
        return ResponseEntity.ok().body(recordResponse);
    }

//    @GetMapping("/records")
//    public ResponseEntity<List<RecordResponse>> findAll(@RequestParam(value = "gymId", required = false) Long gymId,
//                                                        @RequestParam(value = "levelId", required = false) Long levelId,
//                                                        @RequestParam(value = "memberId", required = false) Long memberId) {
//        List<RecordResponse> recordResponses = recordService.findAll(gymId, levelId, memberId);
//        return ResponseEntity.ok().body(recordResponses);
//    }

    @GetMapping("/records")
    @Operation(summary = "기록 목록 조회", description = "페이징 조회")
    @Parameter(name = "gymId", description = "클라이밍장 id")
    @Parameter(name = "levelId", description = "난이도 id")
    @Parameter(name = "memberId", description = "멤버 id")
    @Parameter(name = "page", description = "페이지(페이지는 0페이지부터 시작)")
    @Parameter(name = "size", description = "한 페이지에 조회할 데이터 개수")
    public ResponseEntity<PageDto<RecordResponse>> findPage(@RequestParam(value = "gymId", required = false) Long gymId,
                                                            @RequestParam(value = "levelId", required = false) Long levelId,
                                                            @RequestParam(value = "memberId", required = false) Long memberId,
                                                            @RequestParam(value = "page", required = false, defaultValue = "0") @Min(0) Integer page,
                                                            @RequestParam(value = "size", required = false, defaultValue = "10") @Min(1) Integer size) {

        PageDto<RecordResponse> pageDto = recordService.findPage(gymId, levelId, memberId, page, size);// TODO: 예외 global 하게 처리 필요

        return ResponseEntity.ok().body(pageDto);
    }

    @GetMapping("/myRecords")
    @Operation(summary = "내 기록 목록 조회", description = "사용자의 기록들을 조회합니다.")
    @Parameter(name = "page", description = "페이지(페이지는 0페이지부터 시작)")
    @Parameter(name = "size", description = "한 페이지에 조회할 데이터 개수")
    public ResponseEntity<PageDto<MyRecordResponse>> findPageMy(@RequestMember Member member,
                                                                @RequestParam(value = "page", required = false, defaultValue = "0") @Min(0) Integer page,
                                                                @RequestParam(value = "size", required = false, defaultValue = "10") @Min(1) Integer size) {
        PageDto<MyRecordResponse> pageDto = recordService.findPageMy(member.getId(), page, size);
        return ResponseEntity.ok().body(pageDto);
    }

}
