package com.climingo.climingoApi.report.api;

import com.climingo.climingoApi.report.api.response.ReportReasonsResponse;
import com.climingo.climingoApi.report.api.response.ReportResponse;
import com.climingo.climingoApi.report.application.ReportQuery;
import com.climingo.climingoApi.report.domain.ReportReason;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/reports")
@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportQuery reportQuery;

    @GetMapping("/reasons")
    public ResponseEntity<List<ReportReasonsResponse>> readReportReasons() {
        return ResponseEntity.ok(Arrays.stream(ReportReason.values())
                .map(reason -> new ReportReasonsResponse(reason.getCode(), reason.getDescription()))
                .toList());
    }

    @GetMapping
    public ResponseEntity<List<ReportResponse>> readReports() {
        // TODO 필터링(상태별, 날짜별 등) 기능 추가
        // TODO 회원 role이 admin인 경우에만 접근 가능하도록 추가 구현 필요
        return ResponseEntity.ok().body(reportQuery.readReports());
    }
}
