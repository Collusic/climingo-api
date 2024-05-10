package com.climingo.climingoApi.record.application;

import com.climingo.climingoApi.grade.domain.Grade;
import com.climingo.climingoApi.grade.domain.GradeRepository;
import com.climingo.climingoApi.gym.domain.Gym;
import com.climingo.climingoApi.gym.domain.GymRepository;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.member.domain.MemberRepository;
import com.climingo.climingoApi.record.api.request.RecordCreateRequest;
import com.climingo.climingoApi.record.api.request.RecordUpdateRequest;
import com.climingo.climingoApi.record.api.response.RecordResponse;
import com.climingo.climingoApi.record.domain.Record;
import com.climingo.climingoApi.record.domain.RecordRepository;
import com.climingo.climingoApi.upload.S3Service;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RecordService {

    private final S3Service s3Service;
    private final GymRepository gymRepository;
    private final GradeRepository gradeRepository;
    private final RecordRepository recordRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Record createRecord(RecordCreateRequest request) throws IOException {
        Gym gym = gymRepository.findById(request.getGymId())
                               .orElseThrow(() -> new EntityNotFoundException(request.getGymId() + "is not found"));

        Grade grade = gradeRepository.findById(request.getGradeId())
                                     .orElseThrow(() -> new EntityNotFoundException(request.getGradeId() + "is not found"));

        String videoUrl = s3Service.uploadVideoFile(request.getVideo());

        Record record = Record.builder()
                              .climber(mockMember())
                              .gym(gym)
                              .grade(grade)
                              .content(null)
                              .videoUrl(videoUrl)
                              .recordDate(LocalDateTime.now())
                              .build();

        Record save = recordRepository.save(record);
        return save;
    }

    @Transactional
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

    @Transactional
    public void deleteRecord(Long recordId) {
        Optional<Record> record = recordRepository.findById(recordId);
        record.ifPresent(recordRepository::delete);
    }

    @Transactional(readOnly = true)
    public RecordResponse findById(Long recordId) {
        Record record = recordRepository.findById(recordId)
                                        .orElseThrow(() -> new EntityNotFoundException(recordId + "is not found"));

        RecordResponse recordResponse = new RecordResponse(mockMember(), record, record.getGym(), record.getGrade()); // TODO: climber 정보 연동

        return recordResponse;
    }

    @Transactional(readOnly = true)
    public List<RecordResponse> findAll(Long gymId, Long gradeId, Long memberId) {
        List<Record> records = recordRepository.findAllWithDetails(gymId, gradeId, memberId);

        List<RecordResponse> recordResponses = new ArrayList<>();
        for (Record record : records) {
            recordResponses.add(new RecordResponse(record.getClimber(), record, record.getGym(), record.getGrade()));
        }

        return recordResponses;
    }

    private Member mockMember() {
        return memberRepository.findById(1L)
                               .orElseThrow(() -> new EntityNotFoundException(1L + "is not found"));
    }

}
