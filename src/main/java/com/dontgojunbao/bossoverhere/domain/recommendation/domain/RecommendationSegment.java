package com.dontgojunbao.bossoverhere.domain.recommendation.domain;

import com.dontgojunbao.bossoverhere.domain.spot.domain.Spot;
import com.dontgojunbao.bossoverhere.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommendation_segment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationSegment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "segment_id")
    private Long id;

    @Column(nullable = false)
    private String time;      // "09:00" 같은 HH:mm 포맷

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_spot_id", nullable = false)
    private Spot from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_spot_id", nullable = false)
    private Spot to;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private RecommendationRequest request;

    public static RecommendationSegment fromPlan(
            String time,
            Spot from,
            Spot to
    ) {
        return RecommendationSegment.builder()
                .time(time)
                .from(from)
                .to(to)
                .build();
    }

    public void addRequest(RecommendationRequest request) {
        this.request = request;
    }
}