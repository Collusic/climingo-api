package com.climingo.climingoApi.report.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    int countByRecordId(Long recordId);

    boolean existsByReporterIdAndRecordId(Long reporterId, Long recordId);
}
