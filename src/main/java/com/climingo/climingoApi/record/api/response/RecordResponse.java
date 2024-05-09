package com.climingo.climingoApi.record.api.response;

import com.climingo.climingoApi.grade.domain.Grade;
import com.climingo.climingoApi.gym.domain.Gym;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.record.domain.Record;
import lombok.Getter;

@Getter
public class RecordResponse {

    private ShortMemberResponse climber;

    private ShortRecordResponse record;

    private ShortGymResponse gym;

    private ShortGradeResponse grade;

    public RecordResponse(Member climber, Record record, Gym gym,
                          Grade grade) {
        this.climber = new ShortMemberResponse(null, null, null); // // TODO: climber 정보 연동
        this.record = new ShortRecordResponse(record.getId(), record.getVideoUrl());
        this.gym = new ShortGymResponse(gym.getId(), gym.getName());
        this.grade = new ShortGradeResponse(grade.getId(), grade.getColorName());
    }

}
