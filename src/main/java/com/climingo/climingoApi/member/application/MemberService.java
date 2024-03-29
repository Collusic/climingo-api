package com.climingo.climingoApi.member.application;

import com.climingo.climingoApi.member.api.request.SignUpRequest;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements SignUpService {

    private final MemberRepository memberRepository;

    @Override
    public void signUp(SignUpRequest request) {
        Member member = Member.builder()
            .authId(request.getAuthId())
            .providerType(request.getProviderType())
            .nickname(request.getNickname())
            .profileImage(request.getProfileImage())
            .physicalInfo(request.getPhysicalInfo())
            .build();

        validateAbleToSignUp(member);

        memberRepository.save(member);
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
}
