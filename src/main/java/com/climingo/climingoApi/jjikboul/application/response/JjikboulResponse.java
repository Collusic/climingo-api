package com.climingo.climingoApi.jjikboul.application.response;

import com.climingo.climingoApi.jjikboul.domain.Jjikboul;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.record.api.response.ShortGymResponse;
import com.climingo.climingoApi.record.api.response.ShortLevelResponse;
import com.climingo.climingoApi.record.api.response.ShortMemberResponse;
import com.climingo.climingoApi.record.api.response.ShortRecordResponse;
import com.climingo.climingoApi.record.domain.Record;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record JjikboulResponse(
        ShortMemberResponse memberInfo,
        ShortJjikboulResponse jjikboul,
        ShortGymResponse gym,
        ShortLevelResponse level,
        @JsonProperty("isEditable") boolean editable,
        @JsonProperty("isDeletable") boolean deletable
) {
    public static JjikboulResponse of(Member requestMember, Jjikboul jjikboul) {
        Member jjikboulMember = jjikboul.getMember();
        return new JjikboulResponse(
                new ShortMemberResponse(jjikboulMember.getId(), jjikboulMember.getProfileUrl(), jjikboulMember.getNickname()),
                ShortJjikboulResponse.from(jjikboul),
                new ShortGymResponse(jjikboul.getGym()),
                new ShortLevelResponse(jjikboul.getLevel()),
                requestMember.isSameMember(jjikboulMember) || requestMember.isAdmin(),
                requestMember.isSameMember(jjikboulMember) || requestMember.isAdmin()
        );
    }
}
