package com.climingo.climingoApi.record.api.response;

import lombok.Getter;

@Getter
public class ShortRecordResponse {

    private Long recordId;

    private String videoUrl;

    private String thumbnailUrl;

    public ShortRecordResponse(Long recordId, String videoUrl, String thumbnailUrl) {
        this.recordId = recordId;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

}
