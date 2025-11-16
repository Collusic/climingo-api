package com.climingo.climingoApi.jjikboul.application;

import com.climingo.climingoApi.global.exception.ForbiddenException;
import com.climingo.climingoApi.gym.domain.Gym;
import com.climingo.climingoApi.gym.domain.GymRepository;
import com.climingo.climingoApi.jjikboul.api.request.JjikboulCreateRequest;
import com.climingo.climingoApi.jjikboul.application.response.JjikboulResponse;
import com.climingo.climingoApi.jjikboul.domain.Jjikboul;
import com.climingo.climingoApi.jjikboul.domain.JjikboulQueryRepository;
import com.climingo.climingoApi.jjikboul.domain.JjikboulRepository;
import com.climingo.climingoApi.level.domain.Level;
import com.climingo.climingoApi.level.domain.LevelRepository;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.record.api.response.PageDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JjikboulService implements JjikboulCreateUseCase, JjikboulDeleteUseCase, JjikboulQueryUseCase {

    private final GymRepository gymRepository;
    private final LevelRepository levelRepository;
    private final JjikboulRepository jjikboulRepository;
    private final JjikboulQueryRepository jjikboulQueryRepository;

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
    @Override
    public JjikboulResponse readJjikboul(Member member, Long customProblemId) {
        Jjikboul jjikboul = jjikboulRepository.findById(customProblemId)
                                              .orElseThrow(() -> new EntityNotFoundException(customProblemId + "is not found"));
        return JjikboulResponse.of(member, jjikboul);
    }

    @Transactional(readOnly = true)
    @Override
    public PageDto<JjikboulResponse> readJjikbouls(Member member, Long gymId, Long levelId, Long memberId, Integer page, Integer size) {
        Page<Jjikboul> jjikbouls = jjikboulQueryRepository.findJjikbouls(member, gymId, levelId, memberId, page, size);

        return PageDto.<JjikboulResponse>builder()
                .totalCount(jjikbouls.getTotalElements())
                .resultCount(jjikbouls.getNumberOfElements())
                .totalPage((int) Math.ceil((double) jjikbouls.getTotalElements() / size))
                .page(page)
                .isEnd(jjikbouls.isLast())
                .contents(toRecordResponses(jjikbouls.getContent()))
                .build();
    }

    private List<JjikboulResponse> toRecordResponses(List<Jjikboul> jjikbouls) {
        return jjikbouls.stream()
                        .map(jjikboul -> JjikboulResponse.of(jjikboul.getMember(), jjikboul))
                        .collect(Collectors.toList());
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
