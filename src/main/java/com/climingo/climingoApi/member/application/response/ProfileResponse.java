package com.climingo.climingoApi.member.application.response;

import com.climingo.climingoApi.record.api.response.RecordResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileResponse {

    private MyInfo myInfo;
    private List<RecordResponse> records;

    @Builder
    public ProfileResponse(MyInfo myInfo, List<RecordResponse> records) {
        this.myInfo = myInfo;
        this.records = records;
    }

}
