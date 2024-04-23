package com.climingo.climingoApi.member.domain;

import com.climingo.climingoApi.auth.api.response.MemberInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByAuthIdAndProviderType(String authId, String providerType);

    boolean existsByNickname(String nickname);

    Optional<Member> findByAuthIdAndProviderType(String authId, String providerType);
}
