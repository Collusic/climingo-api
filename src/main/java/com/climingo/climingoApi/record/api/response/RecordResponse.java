package com.climingo.climingoApi.record.api.response;

import com.climingo.climingoApi.level.domain.Level;
import com.climingo.climingoApi.gym.domain.Gym;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.record.domain.Record;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RecordResponse {

    private ShortMemberResponse memberInfo;

    private ShortRecordResponse record;

    private ShortGymResponse gym;

    private ShortLevelResponse level;

    @Builder
    public RecordResponse(Member member, Record record, Gym gym,
                          Level level) {
        this.memberInfo = new ShortMemberResponse(member.getId(), member.getProfileUrl(), member.getNickname());
        this.record = new ShortRecordResponse(record.getId(), record.getVideoUrl(), record.getThumbnailUrl(), record.getCreatedDate());
        this.gym = new ShortGymResponse(gym.getId(), gym.getName());
        this.level = new ShortLevelResponse(level.getId(), level.getColorNameKo(), level.getColorNameEn());
    }

}
