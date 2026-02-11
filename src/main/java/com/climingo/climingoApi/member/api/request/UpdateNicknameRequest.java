package com.climingo.climingoApi.member.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateNicknameRequest {

    @JsonProperty("nickname")
    @Size(min = 2, max = 16)
    private final String nickname;

    @JsonCreator
    public UpdateNicknameRequest(@JsonProperty("nickname") String nickname) {
        this.nickname = nickname;
    }
}
