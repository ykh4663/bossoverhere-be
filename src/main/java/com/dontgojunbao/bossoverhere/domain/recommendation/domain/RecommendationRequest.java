package com.dontgojunbao.bossoverhere.domain.recommendation.domain;

import com.dontgojunbao.bossoverhere.domain.spot.domain.Spot;
import com.dontgojunbao.bossoverhere.domain.user.domain.User;
import com.dontgojunbao.bossoverhere.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recommendation_request")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationRequest extends BaseEntity {
    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "request_date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_category_id", nullable = false)
    private FoodCategory foodCategory;

    @Builder.Default
    @OneToMany(
            mappedBy = "request",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RecommendationRequestCluster> clusterPicks = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;

    @Builder.Default
    @OneToMany(
            mappedBy = "request",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RecommendationSegment> segments = new ArrayList<>();

    public static RecommendationRequest of(
            User user,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            FoodCategory foodCategory,
            Spot spot
    ) {
        return RecommendationRequest.builder()
                .user(user)
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .foodCategory(foodCategory)
                .spot(spot)
                .build();
    }

    // 편의 메서드
    public void addClusterPick(Cluster cluster) {
        this.clusterPicks.add(
                RecommendationRequestCluster.builder()
                        .request(this)
                        .cluster(cluster)
                        .build()
        );
    }
    public void addSegment(RecommendationSegment segment) {
        segment.addRequest(this);
        this.segments.add(segment);
    }

}