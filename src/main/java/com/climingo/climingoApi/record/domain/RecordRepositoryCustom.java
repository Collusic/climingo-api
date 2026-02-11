package com.climingo.climingoApi.record.domain;

import java.util.List;

import com.climingo.climingoApi.member.domain.Member;
import org.springframework.data.domain.Page;

public interface RecordRepositoryCustom {

    List<Record> findAllWithDetails(Long gymId, Long levelId, Long memberId);

    Page<Record> findRecordPage(Member requestMember, Long gymId, Long levelId, Long memberId, Integer page, Integer size);

    Page<Record> findMyRecordPage(Long memberId, Integer page, Integer size);

}
