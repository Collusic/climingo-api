package com.climingo.climingoApi.member.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonDeserialize(builder = UpdateNicknameRequest.UpdateNicknameRequestBuilder.class)
public class UpdateNicknameRequest {

    @JsonProperty("nickname")
    @Size(min = 2, max = 8)
    private final String nickname;
}
