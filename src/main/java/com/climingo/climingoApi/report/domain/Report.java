package com.climingo.climingoApi.report.domain;

import com.climingo.climingoApi.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportReason reason;

    @JoinColumn(name = "REPORTER_ID")
    private Long reporterId;

    @JoinColumn(name = "RECORD_ID")
    private Long recordId;

    @Column
    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @Builder
    public Report(Long id, ReportReason reason, Long reporterId, Long recordId, ReportStatus status) {
        this.id = id;
        this.reason = reason;
        this.reporterId = reporterId;
        this.recordId = recordId;
        this.status = status;
    }

    public static Report create(ReportReason reason, Long reporterId, Long recordId) {
        return Report.builder()
                .reason(reason)
                .reporterId(reporterId)
                .recordId(recordId)
                .status(ReportStatus.PENDING)
                .build();
    }
}
