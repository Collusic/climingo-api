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
        this.record = new ShortRecordResponse(record.getId(), record.getVideoUrl(), record.getThumbnailUrl(), record.getCreatedDate());
        this.gym = new ShortGymResponse(gym.getId(), gym.getName());
        this.level = new ShortLevelResponse(level.getId(), level.getColorNameKo(), level.getColorNameEn());
    }

}
