package com.climingo.climingoApi.record.application;

import com.climingo.climingoApi.grade.domain.Grade;
import com.climingo.climingoApi.grade.domain.GradeRepository;
import com.climingo.climingoApi.gym.domain.Gym;
import com.climingo.climingoApi.gym.domain.GymRepository;
import com.climingo.climingoApi.record.api.request.RecordCreateRequest;
import com.climingo.climingoApi.record.api.request.RecordUpdateRequest;
import com.climingo.climingoApi.record.domain.Record;
import com.climingo.climingoApi.record.domain.RecordRepository;
import com.climingo.climingoApi.upload.S3Service;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RecordService {

    private final S3Service s3Service;
    private final GymRepository gymRepository;
    private final GradeRepository gradeRepository;
    private final RecordRepository recordRepository;

    public Record createRecord(RecordCreateRequest request) throws IOException {
        Gym gym = gymRepository.findById(request.getGymId())
                               .orElseThrow(() -> new EntityNotFoundException(request.getGymId() + "is not found"));

        Grade grade = gradeRepository.findById(request.getGradeId())
                                     .orElseThrow(() -> new EntityNotFoundException(request.getGradeId() + "is not found"));

        String videoUrl = s3Service.uploadVideoFile(request.getVideo());

        Record record = Record.builder()
                              .climber(null)
                              .gym(gym)
                              .grade(grade)
                              .content(null)
                              .videoUrl(videoUrl)
                              .recordDate(LocalDateTime.now())
                              .build();

        Record save = recordRepository.save(record);
        return save;
    }

    public Record updateRecord(Long recordId, RecordUpdateRequest request) {
        Record record = recordRepository.findById(recordId)
                                        .orElseThrow(() -> new EntityNotFoundException(recordId + "is not found"));

        Gym gym = gymRepository.findById(request.getGymId())
                               .orElseThrow(() -> new EntityNotFoundException(request.getGymId() + "is not found"));

        Grade grade = gradeRepository.findById(request.getGradeId())
                                     .orElseThrow(() -> new EntityNotFoundException(request.getGradeId() + "is not found"));

        // TODO: origin 영상 데이터와 updated 영상 데이터가 다른걸 어떻게 알 것인가?
        record.update(gym, grade, null);

        return record;
    }

}
