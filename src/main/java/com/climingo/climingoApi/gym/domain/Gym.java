package com.climingo.climingoApi.gym.domain;

import com.climingo.climingoApi.level.domain.Level;
import jakarta.persistence.*;

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

    @OneToMany(mappedBy = "gym", cascade = CascadeType.REMOVE)
    private List<Level> levels;
}
