package com.climingo.climingoApi.report.domain;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ReportStatus {
    PENDING("PENDING", "검토 대기"),
    UNDER_REVIEW("UNDER_REVIEW", "검토 중"),
    ACTION_TAKEN("ACTION_TAKEN", "조치 완료"),
    REJECTED("REJECTED", "반려됨");

    private final String code;
    private final String description;

    ReportStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    private static final Map<String, ReportStatus> CODE_MAP =
            Stream.of(values()).collect(Collectors.toMap(ReportStatus::getCode, status -> status));

    public static ReportStatus fromCode(String code) {
        return CODE_MAP.get(code);
    }
}
