package com.climingo.climingoApi.gym.api.response;

import lombok.Getter;

@Getter
public class LevelResponse {

    private Long levelId;
    private String colorNameKo;
    private String colorNameEn;

    public LevelResponse(Long levelId, String colorNameKo, String colorNameEn) {
        this.levelId = levelId;
        this.colorNameKo = colorNameKo;
        this.colorNameEn = colorNameEn;
    }

}
