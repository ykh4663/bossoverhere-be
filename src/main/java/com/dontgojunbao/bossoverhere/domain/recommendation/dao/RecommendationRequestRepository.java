package com.dontgojunbao.bossoverhere.domain.recommendation.dao;

import com.dontgojunbao.bossoverhere.domain.recommendation.domain.RecommendationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendationRequestRepository extends JpaRepository<RecommendationRequest, Long> {
    @EntityGraph(
            attributePaths = {
                    "foodCategory",
                    "spot"
            }
    )
    Page<RecommendationRequest> findAllByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {
            "foodCategory",
            "spot",
            "clusterPicks.cluster",
            "segments.from",
            "segments.to"
    })
    Optional<RecommendationRequest> findWithDetailsById(Long requestId);
}