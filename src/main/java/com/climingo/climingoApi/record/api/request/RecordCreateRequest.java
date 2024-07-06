package com.climingo.climingoApi.record.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
public class RecordCreateRequest {

    @NotNull
    private Long gymId;

    @NotNull
    private Long levelId;

    @NotNull
    private String videoUrl;

    public RecordCreateRequest(Long gymId, Long levelId, String videoUrl) {
        this.gymId = gymId;
        this.levelId = levelId;
        this.videoUrl = videoUrl;
    }

}
