package com.climingo.climingoApi.record.api.response;

import lombok.Getter;

@Getter
public class ShortRecordResponse {

    private Long recordId;

    private String videoUrl;

    public ShortRecordResponse(Long recordId, String videoUrl) {
        this.recordId = recordId;
        this.videoUrl = videoUrl;
    }

}
