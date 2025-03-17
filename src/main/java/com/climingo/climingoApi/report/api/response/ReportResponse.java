package com.climingo.climingoApi.report.api.response;

import com.climingo.climingoApi.report.domain.Report;

import java.time.LocalDateTime;

public record ReportResponse(Long id,
                             Long recordId,
                             Long reporterId,
                             String reasonCode,
                             String reasonDescription,
                             String status,
                             LocalDateTime createdDate) {

    public static ReportResponse of(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getRecordId(),
                report.getReporterId(),
                report.getReason().getCode(),
                report.getReason().getDescription(),
                report.getStatus().getCode(),
                report.getCreatedDate());
    }
}
