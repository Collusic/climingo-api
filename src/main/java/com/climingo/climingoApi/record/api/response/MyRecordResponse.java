package com.climingo.climingoApi.record.api.response;

import com.climingo.climingoApi.gym.domain.Gym;
import com.climingo.climingoApi.level.domain.Level;
import com.climingo.climingoApi.record.domain.Record;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MyRecordResponse {

    private ShortRecordResponse record;

    private ShortGymResponse gym;

    private ShortLevelResponse level;

    @Builder
    public MyRecordResponse(Record record, Gym gym, Level level) {
        this.record = ShortRecordResponse.from(record);
        this.gym = new ShortGymResponse(gym);
        this.level = new ShortLevelResponse(level);
    }

}
