package com.climingo.climingoApi.report.domain;

import com.climingo.climingoApi.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Block extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    private Long memberId;

    @JoinColumn(nullable = false)
    private Long recordId;

    @Builder
    public Block(Long id, Long memberId, Long recordId) {
        this.id = id;
        this.memberId = memberId;
        this.recordId = recordId;
    }

    public static Block create(Long memberId, Long recordId) {
        return Block.builder()
                .memberId(memberId)
                .recordId(recordId)
                .build();
    }
}
