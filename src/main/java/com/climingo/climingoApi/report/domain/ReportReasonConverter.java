package com.climingo.climingoApi.report.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ReportReasonConverter implements AttributeConverter<ReportReason, String> {
    @Override
    public String convertToDatabaseColumn(ReportReason reportReason) {
        return reportReason.getCode();
    }

    @Override
    public ReportReason convertToEntityAttribute(String reasonConde) {
        return ReportReason.fromCode(reasonConde);
    }
}
