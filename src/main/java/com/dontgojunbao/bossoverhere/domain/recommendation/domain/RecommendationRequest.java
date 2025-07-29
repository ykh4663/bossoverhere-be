package com.dontgojunbao.bossoverhere.domain.recommendation.domain;

import com.dontgojunbao.bossoverhere.domain.spot.domain.Spot;
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
public class RecommendationRequest {
    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    // 편의 메서드
    public void updateFoodCategory(FoodCategory food) {
        this.foodCategory = food;
    }

    public void addClusterPick(Cluster cluster) {
        RecommendationRequestCluster pick = RecommendationRequestCluster.builder()
                .request(this)
                .cluster(cluster)
                .build();
        this.clusterPicks.add(pick);
    }

}
