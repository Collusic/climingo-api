package com.climingo.climingoApi.record.domain;

import static com.climingo.climingoApi.gym.domain.QGym.gym;
import static com.climingo.climingoApi.level.domain.QLevel.level;
import static com.climingo.climingoApi.member.domain.QMember.member;
import static com.climingo.climingoApi.record.domain.QRecord.record;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

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
                           .innerJoin(record.gym, gym).fetchJoin()
                           .innerJoin(record.level, level).fetchJoin()
                           .where(gymIdEq(gymId),
                                  levelIdEq(levelId),
                                  memberIdEq(memberId))
                           .fetch();
    }

    @Override
    public Page<Record> findRecordPage(Long gymId, Long levelId, Long memberId, Integer page, Integer size) {
        List<Record> contents = queryFactory.selectFrom(record)
                                            .distinct()
                                            .innerJoin(record.member, member).fetchJoin()
                                            .innerJoin(record.gym, gym).fetchJoin()
                                            .innerJoin(record.level, level).fetchJoin()
                                            .where(gymIdEq(gymId),
                                                   levelIdEq(levelId),
                                                   memberIdEq(memberId))
                                            .orderBy(new OrderSpecifier<>(Order.DESC, record.createdDate))
                                            .offset((long) page * size)
                                            .limit(size)
                                            .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(record.count())
                                                .from(record)
                                                .where(gymIdEq(gymId),
                                                       levelIdEq(levelId),
                                                       memberIdEq(memberId));

        Pageable pageable = PageRequest.of(page, size);
        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Record> findMyRecordPage(Long memberId, Integer page, Integer size) {
        List<Record> contents = queryFactory.selectFrom(record)
                                            .innerJoin(record.member, member).fetchJoin()
                                            .innerJoin(record.gym, gym).fetchJoin()
                                            .innerJoin(record.level, level).fetchJoin()
                                            .where(memberIdEq(memberId))
                                            .orderBy(new OrderSpecifier<>(Order.DESC, record.createdDate))
                                            .offset((long) page * size)
                                            .limit(size)
                                            .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(record.count())
                                                .from(record)
                                                .where(memberIdEq(memberId));

        Pageable pageable = PageRequest.of(page, size);
        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
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