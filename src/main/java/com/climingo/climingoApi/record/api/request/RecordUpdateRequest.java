package com.climingo.climingoApi.record.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
public class RecordUpdateRequest {

    private Long gymId;
    private Long gradeId;
    private MultipartFile video;

    public RecordUpdateRequest(Long gymId, Long gradeId, MultipartFile video) {
        this.gymId = gymId;
        this.gradeId = gradeId;
        this.video = video;
    }

}
