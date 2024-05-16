package com.climingo.climingoApi.gym.api.response;

import lombok.Getter;

@Getter
public class LevelResponse {

    private Long id;
    private String colorNameKo;
    private String colorNameEn;

    public LevelResponse(Long id, String colorNameKo, String colorNameEn) {
        this.id = id;
        this.colorNameKo = colorNameKo;
        this.colorNameEn = colorNameEn;
    }

}
