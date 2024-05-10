package com.climingo.climingoApi.member.application;

import com.climingo.climingoApi.member.application.response.MemberInfoResponse;
import com.climingo.climingoApi.member.application.response.ProfileResponse;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.member.domain.MemberRepository1;
import com.climingo.climingoApi.record.api.response.RecordResponse;
import com.climingo.climingoApi.record.domain.Record;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService1 {

    private final MemberRepository1 memberRepository;

    @Transactional(readOnly = true)
    public ProfileResponse findMyInfo(Long memberId) {
        Member member = memberRepository.findMemberWithRecords(memberId);

        List<RecordResponse> recordResponses = new ArrayList<>();
        for (Record record : member.getRecords()) {
            recordResponses.add(new RecordResponse(member, record, record.getGym(), record.getGrade()));
        }

        ProfileResponse profileResponse = ProfileResponse.builder()
                                                         .myInfo(new MemberInfoResponse(member))
                                                         .records(recordResponses)
                                                         .build();
        return profileResponse;
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse findMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                                        .orElseThrow(() -> new EntityNotFoundException(memberId + "is not found"));

        return new MemberInfoResponse(member);
    }

}