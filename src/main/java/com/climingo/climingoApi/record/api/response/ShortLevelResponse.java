package com.climingo.climingoApi.record.api.response;

import com.climingo.climingoApi.level.domain.Level;
import lombok.Getter;

@Getter
public class ShortLevelResponse {

    private final Long levelId;

    private final String colorNameKo;

    private final String colorNameEn;

    public ShortLevelResponse(Level level) {
        this.levelId = level.getId();
        this.colorNameKo = level.getColorNameKo();
        this.colorNameEn = level.getColorNameEn();
    }
}
