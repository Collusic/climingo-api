package com.climingo.climingoApi.report.api;

import com.climingo.climingoApi.report.api.response.ReportReasonsResponse;
import com.climingo.climingoApi.report.domain.ReportReason;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/reports")
@RestController
public class ReportController {

    @GetMapping("/reasons")
    public List<ReportReasonsResponse> readReportReasons() {
        return Arrays.stream(ReportReason.values())
                .map(reason -> new ReportReasonsResponse(reason.getCode(), reason.getDescription()))
                .toList();
    }

}
