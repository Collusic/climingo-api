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
    public List<Record> findAllWithDetails(Long gymId, Long gradeId) {
        return queryFactory.select(record)
                           .from(record)
                           .innerJoin(record.climber, member).fetchJoin()
                           .innerJoin(record.gym, QGym.gym).fetchJoin()
                           .where(gymIdEq(gymId),
                                  gradeIdEq(gradeId))
                           .fetch();
    }

//    private BooleanExpression climberIdEq(Long climberId) {
//        return climberId == null ? null : record.climber.id.eq(climberId);
//    }

    private BooleanExpression gymIdEq(Long gymId) {
        return gymId == null ? null : record.gym.id.eq(gymId);
    }

    private BooleanExpression gradeIdEq(Long gradeId) {
        return gradeId == null ? null : record.grade.id.eq(gradeId);
    }

}