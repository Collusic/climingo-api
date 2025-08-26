package com.climingo.climingoApi.jjikboul.application;

import com.climingo.climingoApi.global.exception.ForbiddenException;
import com.climingo.climingoApi.gym.domain.Gym;
import com.climingo.climingoApi.gym.domain.GymRepository;
import com.climingo.climingoApi.jjikboul.api.request.JjikboulCreateRequest;
import com.climingo.climingoApi.jjikboul.application.response.JjikboulResponse;
import com.climingo.climingoApi.jjikboul.domain.Jjikboul;
import com.climingo.climingoApi.jjikboul.domain.JjikboulRepository;
import com.climingo.climingoApi.level.domain.Level;
import com.climingo.climingoApi.level.domain.LevelRepository;
import com.climingo.climingoApi.member.domain.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JjikboulService implements JjikboulCreateUseCase, JjikboulDeleteUseCase, JjikboulQueryUseCase {

    private final GymRepository gymRepository;
    private final LevelRepository levelRepository;
    private final JjikboulRepository jjikboulRepository;

    @Transactional
    @Override
    public Long create(Member member, JjikboulCreateRequest request) {
        Gym gym = gymRepository.findById(request.gymId())
                .orElseThrow(() -> new EntityNotFoundException(request.gymId() + "is not found"));

        Level level = levelRepository.findById(request.levelId())
                .orElseThrow(() -> new EntityNotFoundException(request.levelId() + "is not found"));

        Jjikboul jjikboul = Jjikboul.builder()
                .gym(gym)
                .level(level)
                .member(member)
                .problemType(request.problemType())
                .problemUrl(request.problemUrl())
                .description(request.description())
                .build();

        return jjikboulRepository.save(jjikboul).getId();
    }

    @Transactional(readOnly = true)
    public JjikboulResponse readJjikboul(Member member, Long customProblemId) {
        Jjikboul jjikboul = jjikboulRepository.findById(customProblemId)
                                              .orElseThrow(() -> new EntityNotFoundException(customProblemId + "is not found"));
        return JjikboulResponse.of(member, jjikboul);
    }

    @Transactional
    @Override
    public void deleteJjikboul(Member requestMember, Long jjikboulId) {
        Jjikboul jjikboul = jjikboulRepository.findById(jjikboulId)
                                              .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 찍볼 문제입니다. requestId=" + jjikboulId));

        if (!jjikboul.isEditable(requestMember)) {
            throw new ForbiddenException("다른 사용자가 업로드한 찍볼은 삭제할 수 없음");
        }

        jjikboulRepository.delete(jjikboul);
    }
}
