package com.climingo.climingoApi.member.domain;

import com.climingo.climingoApi.global.domain.BaseTimeEntity;
import com.climingo.climingoApi.gym.domain.Gym;
import com.climingo.climingoApi.record.domain.Record;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"auth_id", "provider_type"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String authId;

    @Column(nullable = false, length = 10)
    private String providerType;

    @Column(nullable = false, length = 16)
    @Size(min = 2, max = 16)
    private String nickname;

    @Column(nullable = true, length = 400)
    private String profileUrl;

    @Column(nullable = true, length = 50)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    private Gym homeGym;

    @Embedded
    private PhysicalInfo physicalInfo;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Record> records = new ArrayList<>();

    @Builder
    public Member(Long id, String authId, String providerType, String nickname, String profileUrl, String email,
                  Gym homeGym, PhysicalInfo physicalInfo, List<Record> records, UserRole role) {
        this.id = id;
        this.authId = authId;
        this.providerType = providerType;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.email = email;
        this.homeGym = homeGym;
        this.physicalInfo = physicalInfo;
        this.records = records;
        this.role = role;
        if (this.role == null) {
            this.role = UserRole.USER;
        }
    }

    public static Member createGuest() {
        return Member.builder()
            .role(UserRole.GUEST)
            .build();
    }

    public boolean isGuest() {
        return this.role.equals(UserRole.GUEST);
    }

    public boolean isSameMember(Long memberId) {
        return this.id != null && this.id.equals(memberId);
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isSameMember(Member member) {
        return this.isSameMember(member.getId());
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }
}
