package com.climingo.climingoApi.jjikboul.domain;

import com.climingo.climingoApi.member.domain.Member;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.climingo.climingoApi.gym.domain.QGym.gym;
import static com.climingo.climingoApi.jjikboul.domain.QJjikboul.jjikboul;
import static com.climingo.climingoApi.level.domain.QLevel.level;
import static com.climingo.climingoApi.member.domain.QMember.member;
import static com.climingo.climingoApi.report.domain.QBlock.block;

@Repository
@RequiredArgsConstructor
public class JjikboulQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Jjikboul> findJjikbouls(Member requestMember, Long gymId, Long levelId, Long memberId, Integer page, Integer size) {
        List<Jjikboul> jjikbouls = queryFactory.selectFrom(jjikboul)
                .distinct()
                .innerJoin(jjikboul.member, member).fetchJoin()
                .innerJoin(jjikboul.gym, gym).fetchJoin()
                .innerJoin(jjikboul.level, level).fetchJoin()
                .where(gymIdEq(gymId),
                        levelIdEq(levelId),
                        memberIdEq(memberId),
                        jjikboul.blocked.isFalse())
                .orderBy(new OrderSpecifier<>(Order.DESC, jjikboul.createdDate))
                .offset((long) page * size)
                .limit(size)
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(jjikboul.count())
                .from(jjikboul)
                .where(gymIdEq(gymId),
                        levelIdEq(levelId),
                        memberIdEq(memberId),
                        jjikboul.blocked.isFalse());

        Pageable pageable = PageRequest.of(page, size);
        return PageableExecutionUtils.getPage(jjikbouls, pageable, countQuery::fetchOne);
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId == null ? null : jjikboul.member.id.eq(memberId);
    }

    private BooleanExpression gymIdEq(Long gymId) {
        return gymId == null ? null : jjikboul.gym.id.eq(gymId);
    }

    private BooleanExpression levelIdEq(Long levelId) {
        return levelId == null ? null : jjikboul.level.id.eq(levelId);
    }
}
