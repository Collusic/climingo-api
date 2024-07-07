package com.climingo.climingoApi.record.application;

import com.climingo.climingoApi.global.exception.ForbiddenException;
import com.climingo.climingoApi.gym.domain.Gym;
import com.climingo.climingoApi.gym.domain.GymRepository;
import com.climingo.climingoApi.level.domain.Level;
import com.climingo.climingoApi.level.domain.LevelRepository;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.record.api.request.RecordCreateRequest;
import com.climingo.climingoApi.record.api.request.RecordUpdateRequest;
import com.climingo.climingoApi.record.api.response.MyRecordResponse;
import com.climingo.climingoApi.record.api.response.PageDto;
import com.climingo.climingoApi.record.api.response.RecordResponse;
import com.climingo.climingoApi.record.domain.Record;
import com.climingo.climingoApi.record.domain.RecordRepository;
import com.climingo.climingoApi.upload.S3Service;
import com.climingo.climingoApi.upload.ThumbnailExtractor;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecordService {

    private final S3Service s3Service;
    private final ThumbnailExtractor thumbnailExtractor;
    private final GymRepository gymRepository;
    private final LevelRepository levelRepository;
    private final RecordRepository recordRepository;

    @Transactional
    public Record createRecord(Member loginMember, RecordCreateRequest request) throws IOException {
        Gym gym = gymRepository.findById(request.getGymId())
                               .orElseThrow(() -> new EntityNotFoundException(request.getGymId() + "is not found"));

        Level level = levelRepository.findById(request.getLevelId())
                                     .orElseThrow(() -> new EntityNotFoundException(request.getLevelId() + "is not found"));

        String videoUrl = request.getVideoUrl();
        String thumbnailImageUrl = "";
        try {
            thumbnailImageUrl = s3Service.uploadThumbnailImageFile(thumbnailExtractor.extractImage(videoUrl));
        } catch (Exception e) {
            log.error("error: ", e);
            // TODO 썸네일 이미지 저장 안되었을 때 처리
        }

        Record record = Record.builder()
                              .member(loginMember)
                              .gym(gym)
                              .level(level)
                              .content(null)
                              .videoUrl(videoUrl)
                              .thumbnailUrl(thumbnailImageUrl)
                              .build();

        return recordRepository.save(record);
    }

    @Transactional
    public Record updateRecord(Member loginMember, Long recordId, RecordUpdateRequest request) {
        Record record = recordRepository.findById(recordId)
                                        .orElseThrow(() -> new EntityNotFoundException(recordId + "is not found"));

        if (!record.isSameMember(loginMember)) {
            throw new ForbiddenException("다른 사용자가 업로드한 record는 수정할 수 없음");
        }

        Gym gym = gymRepository.findById(request.getGymId())
                               .orElseThrow(() -> new EntityNotFoundException(request.getGymId() + "is not found"));

        Level level = levelRepository.findById(request.getLevelId())
                                     .orElseThrow(() -> new EntityNotFoundException(request.getLevelId() + "is not found"));

        // TODO: origin 영상 데이터와 updated 영상 데이터가 다른걸 어떻게 알 것인가?
        record.update(gym, level, null);

        return record;
    }

    @Transactional
    public void deleteRecord(Member loginMember, Long recordId) {
        Record record = recordRepository.findById(recordId)
                                        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 기록입니다."));

        if (!record.isSameMember(loginMember)) {
            throw new ForbiddenException("다른 사용자가 업로드한 record는 삭제할 수 없음");
        }

        recordRepository.delete(record);
    }

    @Transactional(readOnly = true)
    public RecordResponse findById(Long recordId) {
        Record record = recordRepository.findById(recordId)
                                        .orElseThrow(() -> new EntityNotFoundException(recordId + "is not found"));

        RecordResponse recordResponse = new RecordResponse(record.getMember(), record,
                                                           record.getGym(),
                                                           record.getLevel()); // TODO: climber 정보 연동

        return recordResponse;
    }

    @Transactional(readOnly = true)
    public List<RecordResponse> findAll(Long gymId, Long levelId, Long memberId) {
        List<Record> records = recordRepository.findAllWithDetails(gymId, levelId, memberId);

        List<RecordResponse> recordResponses = new ArrayList<>();
        for (Record record : records) {
            recordResponses.add(
                    new RecordResponse(record.getMember(), record, record.getGym(), record.getLevel()));
        }

        return recordResponses;
    }

    @Transactional(readOnly = true)
    public PageDto<RecordResponse> findPage(Long gymId, Long levelId, Long memberId, Integer page, Integer size) {
        Page<Record> recordPage = recordRepository.findRecordPage(gymId, levelId, memberId, page, size);

        return PageDto.<RecordResponse>builder()
                      .totalCount(recordPage.getTotalElements())
                      .resultCount(recordPage.getNumberOfElements())
                      .totalPage((int) Math.ceil((double) recordPage.getTotalElements() / size))
                      .page(page)
                      .isEnd(recordPage.isLast())
                      .contents(toRecordResponses(recordPage.getContent()))
                      .build();
    }

    @Transactional(readOnly = true)
    public PageDto<MyRecordResponse> findPageMy(Long memberId, Integer page, Integer size) {
        Page<Record> myRecordPage = recordRepository.findMyRecordPage(memberId, page, size);

        return PageDto.<MyRecordResponse>builder()
                      .totalCount(myRecordPage.getTotalElements())
                      .resultCount(myRecordPage.getNumberOfElements())
                      .totalPage((int) Math.ceil((double) myRecordPage.getTotalElements() / size))
                      .page(page)
                      .isEnd(myRecordPage.isLast())
                      .contents(toMyRecordResponses(myRecordPage.getContent()))
                      .build();
    }

    private List<RecordResponse> toRecordResponses(List<Record> records) {
        return records.stream()
                      .map(record -> new RecordResponse(record.getMember(), record, record.getGym(), record.getLevel()))
                      .collect(Collectors.toList());
    }

    private List<MyRecordResponse> toMyRecordResponses(List<Record> records) {
        return records.stream()
                      .map(record -> new MyRecordResponse(record, record.getGym(), record.getLevel()))
                      .collect(Collectors.toList());
    }

}
