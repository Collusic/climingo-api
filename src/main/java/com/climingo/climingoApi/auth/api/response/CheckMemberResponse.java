package com.climingo.climingoApi.auth.api.response;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CheckMemberResponse {

    private final boolean registered;
    private final Map<String, Object> memberInfo;
}
