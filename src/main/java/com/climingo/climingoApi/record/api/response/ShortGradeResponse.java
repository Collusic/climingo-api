package com.climingo.climingoApi.record.api.response;

import lombok.Getter;

@Getter
public class ShortGradeResponse {

    private Long gradeId;

    private String colorName;

    public ShortGradeResponse(Long gradeId, String colorName) {
        this.gradeId = gradeId;
        this.colorName = colorName;
    }

}
