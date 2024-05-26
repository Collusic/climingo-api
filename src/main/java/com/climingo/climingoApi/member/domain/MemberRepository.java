package com.climingo.climingoApi.member.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByAuthIdAndProviderType(String authId, String providerType);

    boolean existsByNickname(String nickname);

    Optional<Member> findByAuthIdAndProviderType(String authId, String providerType);

    @Query("SELECT m FROM Member m JOIN FETCH m.records WHERE m.id = :memberId")
    Member findMemberWithRecords(@Param("memberId") Long memberId);
}
