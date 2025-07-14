package com.climingo.climingoApi.jjikboul.application.response;

import com.climingo.climingoApi.jjikboul.domain.Jjikboul;
import com.climingo.climingoApi.jjikboul.domain.ProblemType;

public record ShortJjikboulResponse(
        Long jjikboulId,
        String problemUrl,
        ProblemType problemType,
        String description
) {
    public static ShortJjikboulResponse from(Jjikboul jjikboul) {
        return new ShortJjikboulResponse(
                jjikboul.getId(),
                jjikboul.getProblemUrl(),
                jjikboul.getProblemType(),
                jjikboul.getDescription());
    }
}
