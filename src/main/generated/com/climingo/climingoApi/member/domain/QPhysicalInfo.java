package com.climingo.climingoApi.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPhysicalInfo is a Querydsl query type for PhysicalInfo
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QPhysicalInfo extends BeanPath<PhysicalInfo> {

    private static final long serialVersionUID = 1593798352L;

    public static final QPhysicalInfo physicalInfo = new QPhysicalInfo("physicalInfo");

    public final NumberPath<Double> armSpan = createNumber("armSpan", Double.class);

    public final NumberPath<Double> height = createNumber("height", Double.class);

    public final NumberPath<Double> weight = createNumber("weight", Double.class);

    public QPhysicalInfo(String variable) {
        super(PhysicalInfo.class, forVariable(variable));
    }

    public QPhysicalInfo(Path<? extends PhysicalInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPhysicalInfo(PathMetadata metadata) {
        super(PhysicalInfo.class, metadata);
    }

}

