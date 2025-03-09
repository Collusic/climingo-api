package com.climingo.climingoApi.report.domain;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ReportReason {
    IRRELEVANT_CONTENT("01", "클라이밍과 관련 없는 이미지/내용"),
    OBSCENE_OR_ABUSIVE("02", "음란, 욕설, 비방 내용"),
    ADVERTISING("03", "개인의 광고, 홍보성 내용"),
    PRIVACY_RISK("04", "개인정보 유출 위험"),
    MISLEADING_HEALTH_INFO("05", "질병 예방·치료 관련 효능·효과 내용"),
    SPAM("06", "게시글 도배"),
    COPYRIGHT_INFRINGEMENT("07", "저작권, 초상권 침해"),
    OTHER("99", "기타");

    private final String code;
    private final String description;

    private static final Map<String, ReportReason> CODE_MAP =
            Stream.of(values()).collect(Collectors.toMap(ReportReason::getCode, reason -> reason));

    ReportReason(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ReportReason fromCode(String code) {
        ReportReason reportReason = CODE_MAP.getOrDefault(code, null);
        if (reportReason == null) {
            throw new IllegalArgumentException("Invalid reason code: " + code);
        }
        return reportReason;
    }
}
