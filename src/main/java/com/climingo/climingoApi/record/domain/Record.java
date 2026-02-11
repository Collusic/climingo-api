package com.climingo.climingoApi.record.domain;

import com.climingo.climingoApi.global.domain.BaseTimeEntity;
import com.climingo.climingoApi.gym.domain.Gym;
import com.climingo.climingoApi.level.domain.Level;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.member.domain.PhysicalInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Record extends BaseTimeEntity {

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
    private String videoUrl;

    @Column(nullable = false, length = 400)
    private String thumbnailUrl;

    @Column(nullable = false)
    private LocalDate climbDate;

    private String content;

    @Embedded
    private PhysicalInfo physicalInfo;

    private boolean blocked;

    @Builder
    public Record(Long id, Member member, Level level, Gym gym, String videoUrl, String thumbnailUrl, String content, LocalDate climbDate,  PhysicalInfo physicalInfo) {
        this.id = id;
        this.member = member;
        this.level = level;
        this.gym = gym;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.content = content;
        this.climbDate = climbDate;
        this.blocked = false;
        this.physicalInfo = physicalInfo;
    }

    public void update(Gym gym, Level level, String videoUrl, Member loginMember) {
        if (!gym.getId().equals(this.gym.getId())) {
            this.gym = gym;
        }
        if (!level.getId().equals(this.level.getId())) {
            this.level = level;
        }

        this.physicalInfo = loginMember.getPhysicalInfo();
        // TODO: origin 영상 데이터와 updated 영상 데이터가 다른걸 어떻게 알 것인가?
    }

    public boolean isSameMember(Member member) {
        return this.member.isSameMember(member.getId());
    }

    public boolean isEditable(Member member) {
        return isSameMember(member) || member.isAdmin();
    }

    public void block() {
        this.blocked = true;
    }

    public void updateClimbDate(LocalDate climbedDate) {
        if (climbedDate != null) {
            this.climbDate = climbedDate;
            return;
        }
        this.climbDate = LocalDate.now();
    }
}
