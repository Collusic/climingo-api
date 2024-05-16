package com.climingo.climingoApi.level.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLevel is a Querydsl query type for Level
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLevel extends EntityPathBase<Level> {

    private static final long serialVersionUID = -69753383L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLevel level = new QLevel("level");

    public final StringPath colorNameEn = createString("colorNameEn");

    public final StringPath colorNameKo = createString("colorNameKo");

    public final com.climingo.climingoApi.gym.domain.QGym gym;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> orderNum = createNumber("orderNum", Integer.class);

    public QLevel(String variable) {
        this(Level.class, forVariable(variable), INITS);
    }

    public QLevel(Path<? extends Level> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLevel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLevel(PathMetadata metadata, PathInits inits) {
        this(Level.class, metadata, inits);
    }

    public QLevel(Class<? extends Level> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.gym = inits.isInitialized("gym") ? new com.climingo.climingoApi.gym.domain.QGym(forProperty("gym"), inits.get("gym")) : null;
    }

}

