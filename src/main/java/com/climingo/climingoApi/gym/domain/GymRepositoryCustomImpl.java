package com.climingo.climingoApi.gym.domain;

import static com.climingo.climingoApi.gym.domain.QGym.gym;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GymRepositoryCustomImpl implements GymRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Gym> findGymPage(String keyword, Long cursorId, int size) {
        List<Gym> gyms = queryFactory
                .selectFrom(gym)
                .where(
                        cursorId(cursorId),
                        containsKeyword(keyword)
                )
                .orderBy(gym.id.asc())
                .limit(size)
                .fetch();
        return gyms;
    }

    @Override
    public long countGyms(String keyword) {
        long total = queryFactory
                .select(gym.count())
                .from(gym)
                .where(
                        containsKeyword(keyword)
                )
                .fetchOne();
        return total;
    }


    private BooleanExpression cursorId(Long cursorId) {
        return cursorId == null ? null : gym.id.gt(cursorId);
    }

    private BooleanExpression containsKeyword(String keyword) {
        return (keyword == null || keyword.isEmpty()) ? null : gym.name.contains(keyword);
    }

}
