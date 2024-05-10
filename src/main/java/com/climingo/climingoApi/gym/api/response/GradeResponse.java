package com.climingo.climingoApi.gym.api.response;

import lombok.Getter;

@Getter
public class GradeResponse {

    private Long id;
    private String colorName;

    public GradeResponse(Long id, String colorName) {
        this.id = id;
        this.colorName = colorName;
    }

}
