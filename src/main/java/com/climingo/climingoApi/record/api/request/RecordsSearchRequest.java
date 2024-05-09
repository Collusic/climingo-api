package com.climingo.climingoApi.record.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RecordsSearchRequest {

    private Long gymId;

    private Long gradeId;

}
