package com.climingo.climingoApi.record.domain;

import com.climingo.climingoApi.grade.domain.Grade;
import com.climingo.climingoApi.gym.domain.Gym;
import com.climingo.climingoApi.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member climber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GRADE_ID")
    private Grade grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GYM_ID")
    private Gym gym;

    @Column(length = 400)
    private String videoUrl;

    private String content;

    private LocalDateTime recordDate;

    @Builder
    public Record(Long id, Member climber, Grade grade, Gym gym, String videoUrl, String content, LocalDateTime recordDate) {
        this.id = id;
        this.climber = climber;
        this.grade = grade;
        this.gym = gym;
        this.videoUrl = videoUrl;
        this.content = content;
        this.recordDate = recordDate;
    }

}
