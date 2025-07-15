package com.climingo.climingoApi.jjikboul.domain;

import com.climingo.climingoApi.global.domain.BaseTimeEntity;
import com.climingo.climingoApi.gym.domain.Gym;
import com.climingo.climingoApi.level.domain.Level;
import com.climingo.climingoApi.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Jjikboul extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LEVEL_ID", nullable = false)
    private Level level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GYM_ID", nullable = false)
    private Gym gym;

    @Column(nullable = false, length = 400)
    private String problemUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROBLEM_TYPE", nullable = false)
    private ProblemType problemType;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "BLOCKED")
    private boolean blocked;

    @Builder
    public Jjikboul(Long id, Member member, Level level, Gym gym, String problemUrl, ProblemType problemType, String description, boolean blocked) {
        this.id = id;
        this.member = member;
        this.level = level;
        this.gym = gym;
        this.problemUrl = problemUrl;
        this.problemType = problemType;
        this.description = description;
        this.blocked = blocked;
    }

    public boolean isEditable(Member member) {
        return isSameMember(member) || member.isAdmin();
    }

    public boolean isSameMember(Member member) {
        return this.member.isSameMember(member.getId());
    }
}
