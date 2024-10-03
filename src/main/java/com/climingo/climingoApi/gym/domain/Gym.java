package com.climingo.climingoApi.gym.domain;

import com.climingo.climingoApi.level.domain.Level;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class Gym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Embedded
    private Address address;

    @Embedded
    private GeoInfo geoInfo;
    @OneToMany(mappedBy = "gym")
    private List<Level> levels;
}
