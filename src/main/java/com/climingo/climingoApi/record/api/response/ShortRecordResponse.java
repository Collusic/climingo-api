package com.climingo.climingoApi.record.api.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
public class ShortRecordResponse {

    private Long recordId;

    private String videoUrl;

    private String thumbnailUrl;

    private String createTime;

    public ShortRecordResponse(Long recordId, String videoUrl, String thumbnailUrl, LocalDateTime createTime) {
        this.recordId = recordId;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.createTime = createTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

}
