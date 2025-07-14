package com.climingo.climingoApi.jjikboul.application;

import com.climingo.climingoApi.jjikboul.api.request.JjikboulCreateRequest;
import com.climingo.climingoApi.jjikboul.application.response.JjikboulResponse;
import com.climingo.climingoApi.jjikboul.domain.Jjikboul;
import com.climingo.climingoApi.jjikboul.domain.JjikboulRepository;
import com.climingo.climingoApi.gym.domain.Gym;
import com.climingo.climingoApi.gym.domain.GymRepository;
import com.climingo.climingoApi.level.domain.Level;
import com.climingo.climingoApi.level.domain.LevelRepository;
import com.climingo.climingoApi.member.domain.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JjikboulService implements JjikboulCreateUseCase, JjikboulQueryUseCase {

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
}
