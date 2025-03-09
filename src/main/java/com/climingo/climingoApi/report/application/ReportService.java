package com.climingo.climingoApi.report.application;

import com.climingo.climingoApi.member.domain.MemberRepository;
import com.climingo.climingoApi.record.application.ReportRecordUsecase;
import com.climingo.climingoApi.record.domain.RecordRepository;
import com.climingo.climingoApi.report.domain.Report;
import com.climingo.climingoApi.report.domain.ReportReason;
import com.climingo.climingoApi.report.domain.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReportService implements ReportRecordUsecase {

    private final RecordRepository recordRepository;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;

    @Transactional
    @Override
    public void reportRecord(Long recordId, Long reporterId, String reasonCode) {
        validateReportable(recordId, reporterId);

        reportRepository.save(Report.create(ReportReason.fromCode(reasonCode), recordId, reporterId));

        if (reportRepository.countByRecordId(recordId) >= 3) {
            // TODO 해당 레코드가 3회 이상 신고 시 차단 조치
        }

        // TODO 디코 알림
    }

    private void validateReportable(Long recordId, Long reporterId) {
        if (!memberRepository.existsById(reporterId)) {
            throw new NoSuchElementException("존재하지 않는 회원입니다.");
        }

        recordRepository.findById(recordId);
        if (!recordRepository.existsById(reporterId)) {
            throw new NoSuchElementException("존재하지 않는 기록입니다.");
        }
    }
}
