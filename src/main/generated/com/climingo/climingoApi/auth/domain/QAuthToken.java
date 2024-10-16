package com.climingo.climingoApi.auth.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuthToken is a Querydsl query type for AuthToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthToken extends EntityPathBase<AuthToken> {

    private static final long serialVersionUID = 2093034968L;

    public static final QAuthToken authToken = new QAuthToken("authToken");

    public final StringPath accessToken = createString("accessToken");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath refreshToken = createString("refreshToken");

    public QAuthToken(String variable) {
        super(AuthToken.class, forVariable(variable));
    }

    public QAuthToken(Path<? extends AuthToken> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuthToken(PathMetadata metadata) {
        super(AuthToken.class, metadata);
    }

}

