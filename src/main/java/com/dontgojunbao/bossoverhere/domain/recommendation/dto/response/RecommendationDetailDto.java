package com.dontgojunbao.bossoverhere.domain.recommendation.dto.response;

import com.dontgojunbao.bossoverhere.domain.recommendation.domain.RecommendationRequest;
import com.dontgojunbao.bossoverhere.domain.spot.dto.SpotDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "추천 상세 응답")
public class RecommendationDetailDto {
    @Schema(description = "추천 요청 ID", example = "123")
    private Long requestId;

    @Schema(description = "추천 요청 날짜 (YYYY-MM-DD)", example = "2025-07-30")
    private LocalDate date;

    @Schema(description = "시작 시간 (HH:mm)", example = "09:00")
    private LocalTime startTime;

    @Schema(description = "종료 시간 (HH:mm)", example = "18:00")
    private LocalTime endTime;

    @Schema(description = "선택된 음식 카테고리 정보")
    private FoodCategoryDto foodCategory;

    @Schema(description = "추천 시작 스팟 정보")
    private SpotDto spot;

    @Schema(description = "선택된 클러스터 간단 정보 리스트")
    private List<ClusterDto> clusterDtos;

    @Schema(description = "추천 이동 구간 리스트")
    private List<RecommendationResponse> segments;

    public static RecommendationDetailDto fromEntity(RecommendationRequest req) {
        List<ClusterDto> clusters = req.getClusterPicks().stream()
                .map(cp -> ClusterDto.from(cp.getCluster()))
                .toList();

        List<RecommendationResponse> segments =
                RecommendationResponse.fromEntityList(req.getSegments());

        return new RecommendationDetailDto(
                req.getId(),
                req.getDate(),
                req.getStartTime(),
                req.getEndTime(),
                FoodCategoryDto.from(req.getFoodCategory()),
                SpotDto.from(req.getSpot()),
                clusters,
                segments
        );
    }
}