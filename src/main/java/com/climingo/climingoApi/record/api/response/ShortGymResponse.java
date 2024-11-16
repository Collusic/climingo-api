package com.climingo.climingoApi.record.api.response;

import com.climingo.climingoApi.gym.domain.Gym;
import lombok.Getter;

@Getter
public class ShortGymResponse {

    private final Long gymId;

    private final String gymName;

    public ShortGymResponse(Gym gym) {
        this.gymId = gym.getId();
        this.gymName = gym.getName();
    }
}
