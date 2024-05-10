package com.climingo.climingoApi.member.domain;

import com.climingo.climingoApi.gym.domain.Gym;
import com.climingo.climingoApi.record.domain.Record;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authId;

    private String providerType;

    private String nickname;

    private String profileImage;

    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    private Gym homeGym;

    @Embedded
    private PhysicalInfo physicalInfo;

    @OneToMany(mappedBy = "climber")
    private List<Record> records = new ArrayList<>();

    @Builder
    public Member(Long id, String authId, String providerType, String nickname, String profileImage, String email,
                  Gym homeGym, PhysicalInfo physicalInfo, List<Record> records) {
        this.id = id;
        this.authId = authId;
        this.providerType = providerType;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.email = email;
        this.homeGym = homeGym;
        this.physicalInfo = physicalInfo;
        this.records = records;
    }

}
