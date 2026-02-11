package com.climingo.climingoApi.record.api.response;

import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.record.domain.Record;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RecordResponse {

    private final ShortMemberResponse memberInfo;

    private final ShortRecordResponse record;

    private final ShortGymResponse gym;

    private final ShortLevelResponse level;

    @JsonProperty("isEditable")
    private final boolean editable;

    @JsonProperty("isDeletable")
    private final boolean deletable;

    @Builder
    public RecordResponse(Member requestMember, Record record) {
        Member recordMember = record.getMember();
        this.memberInfo = new ShortMemberResponse(recordMember.getId(), recordMember.getProfileUrl(), recordMember.getNickname());
        this.record = ShortRecordResponse.from(record);
        this.gym = new ShortGymResponse(record.getGym());
        this.level = new ShortLevelResponse(record.getLevel());
        this.editable = requestMember.isSameMember(recordMember) || requestMember.isAdmin();
        this.deletable = requestMember.isSameMember(recordMember) || requestMember.isAdmin();
    }

}
