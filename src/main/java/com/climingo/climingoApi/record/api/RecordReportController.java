package com.climingo.climingoApi.record.api;

import com.climingo.climingoApi.global.auth.RequestMember;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.record.api.request.ReportRecordRequest;
import com.climingo.climingoApi.record.application.ReportRecordUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RecordReportController {

    private final ReportRecordUsecase reportRecordUsecase;

    @PostMapping("/records/{recordId}/report")
    public ResponseEntity<?> reportRecord(
            @PathVariable("recordId") Long recordId,
            @RequestMember Member member,
            @RequestBody ReportRecordRequest request) {
        reportRecordUsecase.reportRecord(recordId, member.getId(), request.reasonCode());

        return ResponseEntity.ok().build();
    }
}
