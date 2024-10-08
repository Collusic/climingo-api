package com.climingo.climingoApi.level.domain;

import com.climingo.climingoApi.gym.domain.Gym;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
@Entity
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GYM_ID", nullable = false)
    private Gym gym;

    @Column(nullable = false, length = 10)
    private String colorNameKo;

    @Column(nullable = false, length = 10)
    private String colorNameEn;

    @Column(nullable = false)
    private Integer orderNum;
}
