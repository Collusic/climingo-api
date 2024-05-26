package com.climingo.climingoApi.member.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonDeserialize(builder = UpdateNicknameRequest.UpdateNicknameRequestBuilder.class)
public class UpdateNicknameRequest {

    @JsonProperty("nickname")
    @NotNull
    private final String nickname;
}
