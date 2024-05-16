package com.climingo.climingoApi.record.domain;

import static com.climingo.climingoApi.member.domain.QMember.member;
import static com.climingo.climingoApi.record.domain.QRecord.record;

import com.climingo.climingoApi.gym.domain.QGym;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecordRepositoryCustomImpl implements RecordRepositoryCustom {

//    @PersistenceContext
//    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Record> findAllWithDetails(Long gymId, Long levelId, Long memberId) {
        return queryFactory.select(record)
                           .from(record)
                           .innerJoin(record.member, member).fetchJoin()
                           .innerJoin(record.gym, QGym.gym).fetchJoin()
                           .where(gymIdEq(gymId),
                                  levelIdEq(levelId),
                                  memberIdEq(memberId))
                           .fetch();
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId == null ? null : record.member.id.eq(memberId);
    }

    private BooleanExpression gymIdEq(Long gymId) {
        return gymId == null ? null : record.gym.id.eq(gymId);
    }

    private BooleanExpression levelIdEq(Long levelId) {
        return levelId == null ? null : record.level.id.eq(levelId);
    }

}