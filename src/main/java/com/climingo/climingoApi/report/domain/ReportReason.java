package com.climingo.climingoApi.report.domain;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ReportReason {
    IRRELEVANT_CONTENT("01", "서비스와 관련 없는 영상입니다."),
    OBSCENE_OR_ABUSIVE("02", "음란, 욕설, 혐오유발, 비방성 내용입니다."),
    ADVERTISING("03", "개인의 광고나 홍보성 내용입니다."),
    PRIVACY_RISK("04", "개인정보 유출의 위험이 있습니다."),
    SPAM("05", "게시글을 도배했습니다."),
    COPYRIGHT_INFRINGEMENT("06", "저작권이나 초상권을 침해한 영상입니다."),
    ;

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
