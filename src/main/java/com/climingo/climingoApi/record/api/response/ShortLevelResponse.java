package com.climingo.climingoApi.record.api.response;

import lombok.Getter;

@Getter
public class ShortLevelResponse {

    private Long levelId;

    private String colorNameKo;

    private String colorNameEn;

    public ShortLevelResponse(Long levelId, String colorNameKo, String colorNameEn) {
        this.levelId = levelId;
        this.colorNameKo = colorNameKo;
        this.colorNameEn = colorNameEn;
    }

}
