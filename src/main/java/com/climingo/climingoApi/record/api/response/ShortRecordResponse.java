package com.climingo.climingoApi.record.api.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.climingo.climingoApi.record.domain.Record;
import lombok.Getter;

@Getter
public class ShortRecordResponse {

    private final Long recordId;

    private final String videoUrl;

    private final String thumbnailUrl;

    private final String createTime;

    private final String climbDate;

    public ShortRecordResponse(Long recordId, String videoUrl, String thumbnailUrl, LocalDateTime createTime, LocalDate climbDate) {
        this.recordId = recordId;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.createTime = createTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        this.climbDate = createTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static ShortRecordResponse from(Record record) {
        return new ShortRecordResponse(
                record.getId(),
                record.getVideoUrl(),
                record.getThumbnailUrl(),
                record.getCreatedDate(),
                record.getClimbDate());
    }
}
