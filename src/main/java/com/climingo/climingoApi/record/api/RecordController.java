package com.climingo.climingoApi.record.api;

import com.climingo.climingoApi.record.api.request.RecordCreateRequest;
import com.climingo.climingoApi.record.api.request.RecordUpdateRequest;
import com.climingo.climingoApi.record.api.response.PageDto;
import com.climingo.climingoApi.record.api.response.RecordResponse;
import com.climingo.climingoApi.record.application.RecordService;
import com.climingo.climingoApi.record.domain.Record;
import jakarta.validation.constraints.Min;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RecordController {

    private final RecordService recordService;

    @PostMapping("/records")
    public ResponseEntity<Long> create(@ModelAttribute RecordCreateRequest request)
            throws IOException, InterruptedException {
        Record record = recordService.createRecord(request);
        return ResponseEntity.ok().body(record.getId());
    }

    @PatchMapping("/records/{recordId}")
    public ResponseEntity<Long> update(@PathVariable("recordId") Long recordId, @ModelAttribute RecordUpdateRequest request) {
        Record record = recordService.updateRecord(recordId, request);
        return ResponseEntity.ok().body(record.getId());
    }

    @DeleteMapping("/records/{recordId}")
    public ResponseEntity<Void> delete(@PathVariable("recordId") Long recordId) {
        recordService.deleteRecord(recordId);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/records/{recordId}")
    public ResponseEntity<RecordResponse> find(@PathVariable("recordId") Long recordId) {
        RecordResponse recordResponse = recordService.findById(recordId);
        return ResponseEntity.ok().body(recordResponse);
    }

    @GetMapping("/records")
    public ResponseEntity<List<RecordResponse>> findAll(@RequestParam(value = "gymId", required = false) Long gymId,
                                                        @RequestParam(value = "levelId", required = false) Long levelId,
                                                        @RequestParam(value = "memberId", required = false) Long memberId) {
        List<RecordResponse> recordResponses = recordService.findAll(gymId, levelId, memberId);
        return ResponseEntity.ok().body(recordResponses);
    }

    @GetMapping("/records/paged")
    public ResponseEntity<PageDto<RecordResponse>> findPage(@RequestParam(value = "gymId", required = false) Long gymId,
                                         @RequestParam(value = "levelId", required = false) Long levelId,
                                         @RequestParam(value = "memberId", required = false) Long memberId,
                                         @RequestParam(value = "page", required = false, defaultValue = "0") @Min(0) Integer page,
                                         @RequestParam(value = "size", required = false, defaultValue = "10") @Min(1) Integer size) {

        PageDto<RecordResponse> pageDto = recordService.findPage(gymId, levelId, memberId, page, size);// TODO: 예외 global 하게 처리 필요

        return ResponseEntity.ok().body(pageDto);
    }

}
