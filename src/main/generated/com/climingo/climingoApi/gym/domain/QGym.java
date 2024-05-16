package com.climingo.climingoApi.gym.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGym is a Querydsl query type for Gym
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGym extends EntityPathBase<Gym> {

    private static final long serialVersionUID = 693192697L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGym gym = new QGym("gym");

    public final QAddress address;

    public final QGeoInfo geoInfo;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.climingo.climingoApi.level.domain.Level, com.climingo.climingoApi.level.domain.QLevel> levels = this.<com.climingo.climingoApi.level.domain.Level, com.climingo.climingoApi.level.domain.QLevel>createList("levels", com.climingo.climingoApi.level.domain.Level.class, com.climingo.climingoApi.level.domain.QLevel.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public QGym(String variable) {
        this(Gym.class, forVariable(variable), INITS);
    }

    public QGym(Path<? extends Gym> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGym(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGym(PathMetadata metadata, PathInits inits) {
        this(Gym.class, metadata, inits);
    }

    public QGym(Class<? extends Gym> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new QAddress(forProperty("address")) : null;
        this.geoInfo = inits.isInitialized("geoInfo") ? new QGeoInfo(forProperty("geoInfo")) : null;
    }

}

