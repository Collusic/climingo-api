package com.climingo.climingoApi.gym.api.response;

import lombok.Getter;

@Getter
public class LevelResponse {

    private Long id;
    private String colorName;

    public LevelResponse(Long id, String colorName) {
        this.id = id;
        this.colorName = colorName;
    }

}
