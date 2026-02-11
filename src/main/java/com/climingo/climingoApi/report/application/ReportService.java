package com.climingo.climingoApi.report.application;

import com.climingo.climingoApi.member.domain.MemberRepository;
import com.climingo.climingoApi.record.application.ReportRecordUsecase;
import com.climingo.climingoApi.record.domain.Record;
import com.climingo.climingoApi.record.domain.RecordRepository;
import com.climingo.climingoApi.report.api.response.ReportResponse;
import com.climingo.climingoApi.report.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReportService implements ReportRecordUsecase, ReportQuery {

    private final RecordRepository recordRepository;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final BlockRepository blockRepository;

    @Transactional
    @Override
    public void reportRecord(Long recordId, Long reporterId, String reasonCode) {
        validateReportable(recordId, reporterId);

        reportRepository.save(Report.create(ReportReason.fromCode(reasonCode), recordId, reporterId));

        if (reportRepository.countByRecordId(recordId) >= 3) {
            blockRecord(recordId); // 해당 레코드가 3회 이상 신고 시 차단 조치
        }

        blockRepository.save(Block.create(reporterId, recordId));

        // TODO 디코 알림
    }

    private void validateReportable(Long recordId, Long reporterId) {
        if (!memberRepository.existsById(reporterId)) {
            throw new NoSuchElementException("존재하지 않는 회원입니다.");
        }

        if (!recordRepository.existsById(recordId)) {
            throw new NoSuchElementException("존재하지 않는 기록입니다.");
        }

        if (reportRepository.existsByReporterIdAndRecordId(reporterId, recordId)) {
            throw new IllegalArgumentException("이미 신고한 기록입니다.");
        }
    }

    private void blockRecord(Long recordId) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 기록입니다."));
        record.block();
    }

    @Override
    public List<ReportResponse> readReports() {
        return reportRepository.findAll().stream().map(ReportResponse::of).toList();
    }
}
