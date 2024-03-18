package com.climingo.climingoApi.record.domain;

import com.climingo.climingoApi.grade.domain.Grade;
import com.climingo.climingoApi.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member climber;

    @ManyToOne(fetch = FetchType.LAZY)
    private Grade grade;

    private String videoUrl;

    private String content;

    private LocalDateTime recordDate;
}
