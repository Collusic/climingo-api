package com.climingo.climingoApi.member.application;

import com.climingo.climingoApi.auth.api.request.SignUpRequest;
import com.climingo.climingoApi.auth.api.response.MemberInfo;
import com.climingo.climingoApi.auth.application.SignInService;
import com.climingo.climingoApi.auth.application.SignUpService;
import com.climingo.climingoApi.member.application.response.MemberInfoResponse;
import com.climingo.climingoApi.member.application.response.ProfileResponse;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.member.domain.MemberRepository;
import com.climingo.climingoApi.record.api.response.RecordResponse;
import com.climingo.climingoApi.record.domain.Record;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements SignUpService, SignInService, MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public MemberInfo signUp(SignUpRequest request) {
        Member member = Member.builder()
            .authId(request.getAuthId())
            .providerType(request.getProviderType())
            .nickname(request.getNickname())
            .profileUrl(request.getProfileUrl())
            .physicalInfo(request.getPhysicalInfo())
            .build();

        validateAbleToSignUp(member);

        member = memberRepository.save(member);
        return new MemberInfo(member);
    }

    private void validateAbleToSignUp(Member member) {
        // 중복 가입 여부 확인
        if (isDuplicatedMember(member.getAuthId(), member.getProviderType())) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        if (isDuplicatedNickname(member.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
    }

    private boolean isDuplicatedNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    private boolean isDuplicatedMember(String authId, String providerType) {
        return memberRepository.existsByAuthIdAndProviderType(authId, providerType);
    }

    @Override
    public MemberInfo findEnrolledMemberInfoByAuthIdAndProviderType(String authId,
        String providerType) {
        Member member = memberRepository.findByAuthIdAndProviderType(authId, providerType)
            .orElseThrow(() -> new NoSuchElementException("등록되지 않은 사용자. 회원가입을 먼저 진행하세요."));

        return new MemberInfo(member);
    }

    @Override

    @Transactional(readOnly = true)
    public ProfileResponse findMyInfo(Long memberId) {
        Member member = memberRepository.findMemberWithRecords(memberId);

        List<RecordResponse> recordResponses = new ArrayList<>();
        for (Record record : member.getRecords()) {
            recordResponses.add(new RecordResponse(member, record, record.getGym(), record.getLevel()));
        }

        ProfileResponse profileResponse = ProfileResponse.builder()
            .myInfo(new MemberInfoResponse(member))
            .records(recordResponses)
            .build();
        return profileResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public MemberInfoResponse findMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException(memberId + "is not found"));

        return new MemberInfoResponse(member);
    }

    @Override
    public void updateNickname(Member member, Long memberId, String nickname) {
        if (!member.isSameMember(memberId)) {
            throw new AccessDeniedException("다른 사용자의 닉네임은 변경할 수 없음");
        }

        if (isDuplicated(nickname)) {
            throw new DataIntegrityViolationException("Nickname already exists: " + nickname);
        }


        member.updateNickname(nickname);
        memberRepository.save(member);
    }

    private boolean isDuplicated(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
