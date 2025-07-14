package com.climingo.climingoApi.jjikboul.api.response;

public record JjikboulCreateResponse(Long id) {
    public static JjikboulCreateResponse of(Long id) {
        return new JjikboulCreateResponse(id);
    }
}
