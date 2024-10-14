package com.climingo.climingoApi.record.api.response;

import com.climingo.climingoApi.level.domain.Level;
import com.climingo.climingoApi.gym.domain.Gym;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.record.domain.Record;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RecordResponse {

    private final ShortMemberResponse memberInfo;

    private final ShortRecordResponse record;

    private final ShortGymResponse gym;

    private final ShortLevelResponse level;

    private final boolean isEditable;

    private final boolean isDeletable;

    @Builder
    public RecordResponse(Member requestMember, Record record) {
        Member recordMember = record.getMember();
        this.memberInfo = new ShortMemberResponse(recordMember.getId(), recordMember.getProfileUrl(), recordMember.getNickname());
        this.record = new ShortRecordResponse(record.getId(), record.getVideoUrl(), record.getThumbnailUrl(), record.getCreatedDate());
        this.gym = new ShortGymResponse(record.getGym());
        this.level = new ShortLevelResponse(record.getLevel());
        this.isEditable = requestMember.isSameMember(recordMember) || requestMember.isAdmin();
        this.isDeletable = requestMember.isSameMember(recordMember) || requestMember.isAdmin();
    }

}
