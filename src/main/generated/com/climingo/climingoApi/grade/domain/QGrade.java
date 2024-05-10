package com.climingo.climingoApi.grade.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGrade is a Querydsl query type for Grade
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGrade extends EntityPathBase<Grade> {

    private static final long serialVersionUID = 897221817L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGrade grade = new QGrade("grade");

    public final StringPath colorName = createString("colorName");

    public final com.climingo.climingoApi.gym.domain.QGym gym;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> orderNum = createNumber("orderNum", Integer.class);

    public QGrade(String variable) {
        this(Grade.class, forVariable(variable), INITS);
    }

    public QGrade(Path<? extends Grade> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGrade(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGrade(PathMetadata metadata, PathInits inits) {
        this(Grade.class, metadata, inits);
    }

    public QGrade(Class<? extends Grade> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.gym = inits.isInitialized("gym") ? new com.climingo.climingoApi.gym.domain.QGym(forProperty("gym"), inits.get("gym")) : null;
    }

}

