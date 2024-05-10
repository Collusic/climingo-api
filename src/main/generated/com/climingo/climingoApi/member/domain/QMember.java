package com.climingo.climingoApi.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1574182789L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final StringPath authId = createString("authId");

    public final StringPath email = createString("email");

    public final com.climingo.climingoApi.gym.domain.QGym homeGym;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final QPhysicalInfo physicalInfo;

    public final StringPath profileImage = createString("profileImage");

    public final StringPath providerType = createString("providerType");

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.homeGym = inits.isInitialized("homeGym") ? new com.climingo.climingoApi.gym.domain.QGym(forProperty("homeGym"), inits.get("homeGym")) : null;
        this.physicalInfo = inits.isInitialized("physicalInfo") ? new QPhysicalInfo(forProperty("physicalInfo")) : null;
    }

}

