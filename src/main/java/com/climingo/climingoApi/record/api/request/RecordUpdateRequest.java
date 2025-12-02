package com.climingo.climingoApi.record.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class RecordUpdateRequest {

    @NotNull
    private Long gymId;

    @NotNull
    private Long levelId;

    @NotNull
    private MultipartFile video;

    //    @NotNull todo: front 개발 완료 후 not null 처리 예정
    private LocalDate climbDate;

    public RecordUpdateRequest(Long gymId, Long levelId, MultipartFile video, LocalDate climbDate) {
        this.gymId = gymId;
        this.levelId = levelId;
        this.video = video;
        this.climbDate = climbDate;
    }

}
