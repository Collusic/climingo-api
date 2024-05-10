package com.climingo.climingoApi.gym.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGeoInfo is a Querydsl query type for GeoInfo
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QGeoInfo extends BeanPath<GeoInfo> {

    private static final long serialVersionUID = -316096803L;

    public static final QGeoInfo geoInfo = new QGeoInfo("geoInfo");

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public QGeoInfo(String variable) {
        super(GeoInfo.class, forVariable(variable));
    }

    public QGeoInfo(Path<? extends GeoInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGeoInfo(PathMetadata metadata) {
        super(GeoInfo.class, metadata);
    }

}

