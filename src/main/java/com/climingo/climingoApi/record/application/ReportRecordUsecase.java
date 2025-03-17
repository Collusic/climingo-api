package com.climingo.climingoApi.record.application;

public interface ReportRecordUsecase {
    void reportRecord(Long recordId, Long reporterId, String reasonCode);
}
