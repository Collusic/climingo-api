package com.climingo.climingoApi.record.api.response;

import lombok.Getter;

@Getter
public class ShortGymResponse {

    private Long gymId;

    private String gymName;

    public ShortGymResponse(Long gymId, String gymName) {
        this.gymId = gymId;
        this.gymName = gymName;
    }

}
