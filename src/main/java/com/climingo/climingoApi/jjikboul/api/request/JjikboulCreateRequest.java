package com.climingo.climingoApi.jjikboul.api.request;

import com.climingo.climingoApi.jjikboul.domain.ProblemType;

public record JjikboulCreateRequest(
        Long gymId,
        Long levelId,
        String problemUrl,
        ProblemType problemType,
        String description
) {
}
