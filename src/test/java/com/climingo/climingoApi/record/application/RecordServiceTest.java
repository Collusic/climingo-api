package com.climingo.climingoApi.record.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.climingo.climingoApi.global.exception.ForbiddenException;
import com.climingo.climingoApi.gym.domain.Gym;
import com.climingo.climingoApi.gym.domain.GymRepository;
import com.climingo.climingoApi.level.domain.Level;
import com.climingo.climingoApi.level.domain.LevelRepository;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.member.domain.UserRole;
import com.climingo.climingoApi.record.api.request.RecordCreateRequest;
import com.climingo.climingoApi.record.api.request.RecordUpdateRequest;
import com.climingo.climingoApi.record.domain.Record;
import com.climingo.climingoApi.record.domain.RecordRepository;
import com.climingo.climingoApi.upload.S3Service;
import com.climingo.climingoApi.upload.ThumbnailExtractor;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

@DisplayName("RecordService Unit Tests")
public class RecordServiceTest {

    private RecordService recordService;

    private RecordRepository recordRepository;
    private GymRepository gymRepository;
    private LevelRepository levelRepository;

    @BeforeEach
    void setUp() throws IOException {
        S3Service s3Service = mock(S3Service.class);
        ThumbnailExtractor thumbnailExtractor = mock(ThumbnailExtractor.class);
        gymRepository = mock(GymRepository.class);
        levelRepository = mock(LevelRepository.class);
        recordRepository = mock(RecordRepository.class);

        when(s3Service.uploadThumbnailImageFile(any())).thenReturn("http://mock-thumbnail-url");
        when(thumbnailExtractor.extractImage(any())).thenReturn(mock(File.class));

        recordService = new RecordService(
            s3Service, thumbnailExtractor, gymRepository, levelRepository, recordRepository);
    }

    @Test
    @DisplayName("기록 생성 테스트 - 정상적인 요청의 경우 로그인한 사용자가 작성자인 기록이 생성된다.")
    void create_test() throws IOException {
        Long mockGymId = 1L;
        Long mockLevelId = 1L;
        String mockVideoUrl = "http://mock-video-url";

        Member loginMember = Member.builder()
            .id(99999L)
            .role(UserRole.USER)
            .build();
        RecordCreateRequest request = new RecordCreateRequest(mockGymId, mockLevelId, mockVideoUrl, LocalDate.now());

        Record expected = Record.builder()
            .member(loginMember)
            .videoUrl("http://mock-video-url")
            .thumbnailUrl("http://mock-thumbnail-url")
            .climbDate(request.getClimbDate())
            .build();

        when(gymRepository.findById(anyLong())).thenReturn(Optional.of(mock(Gym.class)));
        when(levelRepository.findById(anyLong())).thenReturn(Optional.of(mock(Level.class)));
        when(recordRepository.save(any())).thenReturn(expected);

        Record actual = recordService.createRecord(loginMember, request);

        assertEquals(expected.getMember(), actual.getMember());
        assertEquals(expected.getVideoUrl(), actual.getVideoUrl());
        assertEquals(expected.getThumbnailUrl(), actual.getThumbnailUrl());
        assertEquals(expected.getClimbDate(), actual.getClimbDate());
    }

    @Test
    @DisplayName("기록 수정 테스트 - 로그인한 사용자와 수정하려는 기록의 작성자가 동일한 경우 정상적으로 수정된다.")
    void update_test() {
        Member loginMember = Member.builder()
            .id(99999L)
            .role(UserRole.USER)
            .build();

        Gym gym1 = mock(Gym.class);
        when(gym1.getId()).thenReturn(1L);

        Gym gym2 = mock(Gym.class);
        when(gym2.getId()).thenReturn(2L);

        Level level1 = mock(Level.class);
        when(level1.getId()).thenReturn(1L);

        Level level2 = mock(Level.class);
        when(level2.getId()).thenReturn(2L);

        Record before = Record.builder()
            .member(loginMember)
            .gym(gym1)
            .level(level1)
            .videoUrl("http://mock-video-url")
            .thumbnailUrl("http://mock-thumbnail-url")
            .build();

        Record expected = Record.builder()
            .member(loginMember)
            .gym(gym2)
            .level(level2)
            .videoUrl("http://mock-video-url")
            .thumbnailUrl("http://mock-thumbnail-url")
            .build();

        when(gymRepository.findById(1L)).thenReturn(Optional.of(gym1));
        when(levelRepository.findById(1L)).thenReturn(Optional.of(level1));
        when(gymRepository.findById(2L)).thenReturn(Optional.of(gym2));
        when(levelRepository.findById(2L)).thenReturn(Optional.of(level2));
        when(recordRepository.findById(1L)).thenReturn(Optional.of(before));
        when(recordRepository.save(any())).thenReturn(expected);

        Long updatedGymId = 2L;
        Long updatedLevelId = 2L;
        MultipartFile mockVideo = mock(MultipartFile.class);

        RecordUpdateRequest request = new RecordUpdateRequest(updatedGymId, updatedLevelId, mockVideo);
        Record actual = recordService.updateRecord(loginMember, 1L, request);

        assertEquals(expected.getMember(), actual.getMember());
        assertEquals(2L, actual.getGym().getId());
        assertEquals(2L, actual.getGym().getId());
    }

    @Test
    @DisplayName("기록 수정 테스트 - 로그인한 사용자와 수정하려는 기록의 작성자 다른 경우 권한 제한(forbidden)예외가 발생한다.")
    void update_test_when_not_same_member_then_throw_exception() {
        Member loginMember = Member.builder()
            .id(99999L)
            .role(UserRole.USER)
            .build();

        Gym gym1 = mock(Gym.class);
        when(gym1.getId()).thenReturn(1L);

        Gym gym2 = mock(Gym.class);
        when(gym2.getId()).thenReturn(2L);

        Level level1 = mock(Level.class);
        when(level1.getId()).thenReturn(1L);

        Level level2 = mock(Level.class);
        when(level2.getId()).thenReturn(2L);

        Record before = Record.builder()
            .member(Member.builder().id(100000L).role(UserRole.USER).build())
            .gym(gym1)
            .level(level1)
            .videoUrl("http://mock-video-url")
            .thumbnailUrl("http://mock-thumbnail-url")
            .build();

        when(gymRepository.findById(1L)).thenReturn(Optional.of(gym1));
        when(levelRepository.findById(1L)).thenReturn(Optional.of(level1));
        when(gymRepository.findById(2L)).thenReturn(Optional.of(gym2));
        when(levelRepository.findById(2L)).thenReturn(Optional.of(level2));
        when(recordRepository.findById(1L)).thenReturn(Optional.of(before));

        Long updatedGymId = 2L;
        Long updatedLevelId = 2L;
        MultipartFile mockVideo = mock(MultipartFile.class);

        RecordUpdateRequest request = new RecordUpdateRequest(updatedGymId, updatedLevelId, mockVideo);

        Throwable exception = assertThrows(ForbiddenException.class,
            () -> recordService.updateRecord(loginMember, 1L, request));
        assertEquals("다른 사용자가 업로드한 record는 수정할 수 없음", exception.getMessage());
    }

    @Test
    @DisplayName("기록 삭제 테스트 - 로그인한 사용자와 삭제하려는 기록의 작성자가 동일한 경우 정상적으로 삭제된다.")
    void delete_test() {
        Member loginMember = Member.builder()
            .id(99999L)
            .role(UserRole.USER)
            .build();

        Record before = Record.builder()
            .member(loginMember)
            .build();

        when(recordRepository.findById(1L)).thenReturn(Optional.of(before));
        doNothing().when(recordRepository).delete(any());

        recordService.deleteRecord(loginMember, 1L);
        when(recordRepository.findById(1L)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(NoSuchElementException.class,
            () -> recordService.deleteRecord(loginMember, 1L));
        assertEquals("존재하지 않는 기록입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("기록 삭제 테스트 - 로그인한 사용자와 삭제하려는 기록의 작성자 다른 경우 권한 제한(forbidden)예외가 발생한다.")
    void delete_test_when_not_same_member_then_throw_exception() {
        Member loginMember = Member.builder()
            .id(99999L)
            .role(UserRole.USER)
            .build();

        Record before = Record.builder()
            .member(Member.builder().role(UserRole.USER).id(100000L).build())
            .build();

        when(recordRepository.findById(1L)).thenReturn(Optional.of(before));

        Throwable exception = assertThrows(ForbiddenException.class,
            () -> recordService.deleteRecord(loginMember, 1L));
        assertEquals("다른 사용자가 업로드한 record는 삭제할 수 없음", exception.getMessage());
    }

    // TODO
    @Test
    @DisplayName("기록 조회 테스트 - ")
    void read_test() {
    }
}