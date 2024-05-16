package com.climingo.climingoApi.record.api.response;

import lombok.Getter;

@Getter
public class ShortLevelResponse {

    private Long levelId;

    private String colorName;

    public ShortLevelResponse(Long levelId, String colorName) {
        this.levelId = levelId;
        this.colorName = colorName;
    }

}
