package com.climingo.climingoApi.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository1 extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m JOIN FETCH m.records WHERE m.id = :memberId")
    Member findMemberWithRecords(@Param("memberId") Long memberId);

}
