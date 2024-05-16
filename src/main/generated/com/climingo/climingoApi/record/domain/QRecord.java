package com.climingo.climingoApi.record.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecord is a Querydsl query type for Record
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecord extends EntityPathBase<Record> {

    private static final long serialVersionUID = -505997645L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecord record = new QRecord("record");

    public final StringPath content = createString("content");

    public final com.climingo.climingoApi.gym.domain.QGym gym;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.climingo.climingoApi.level.domain.QLevel level;

    public final com.climingo.climingoApi.member.domain.QMember member;

    public final DateTimePath<java.time.LocalDateTime> recordDate = createDateTime("recordDate", java.time.LocalDateTime.class);

    public final StringPath thumbnailUrl = createString("thumbnailUrl");

    public final StringPath videoUrl = createString("videoUrl");

    public QRecord(String variable) {
        this(Record.class, forVariable(variable), INITS);
    }

    public QRecord(Path<? extends Record> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecord(PathMetadata metadata, PathInits inits) {
        this(Record.class, metadata, inits);
    }

    public QRecord(Class<? extends Record> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.gym = inits.isInitialized("gym") ? new com.climingo.climingoApi.gym.domain.QGym(forProperty("gym"), inits.get("gym")) : null;
        this.level = inits.isInitialized("level") ? new com.climingo.climingoApi.level.domain.QLevel(forProperty("level"), inits.get("level")) : null;
        this.member = inits.isInitialized("member") ? new com.climingo.climingoApi.member.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

