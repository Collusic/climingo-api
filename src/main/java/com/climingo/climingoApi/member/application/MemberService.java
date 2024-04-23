package com.climingo.climingoApi.member.application;

import com.climingo.climingoApi.auth.api.request.SignUpRequest;
import com.climingo.climingoApi.auth.api.response.MemberInfo;
import com.climingo.climingoApi.auth.application.SignInService;
import com.climingo.climingoApi.auth.application.SignUpService;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.member.domain.MemberRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService implements SignUpService, SignInService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public MemberInfo signUp(SignUpRequest request) {
        Member member = Member.builder()
            .authId(request.getAuthId())
            .providerType(request.getProviderType())
            .nickname(request.getNickname())
            .profileImage(request.getProfileImage())
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
}
