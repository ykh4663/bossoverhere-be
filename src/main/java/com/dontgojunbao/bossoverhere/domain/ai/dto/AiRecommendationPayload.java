package com.dontgojunbao.bossoverhere.domain.ai.dto;

import com.dontgojunbao.bossoverhere.domain.recommendation.dto.request.RecommendationRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "AI 추천 요청")

public class AiRecommendationPayload {
    @Schema(description = "추천 요청 날짜 (yyyy-MM-dd)", example = "2025-07-29")
    private String date;

    @Schema(description = "시작 시각 (HH:mm)", example = "09:00")
    private String startTime;

    @Schema(description = "종료 시각 (HH:mm)", example = "18:00")
    private String endTime;

    @Schema(description = "음식 카테고리 ID", example = "5")
    private Long foodCategoryId;

    @Schema(description = "출발지 스팟 ID", example = "10")
    private Long spotId;

    @Schema(description = "선택된 클러스터 ID 리스트", example = "[1,2,5]")
    private List<Long> clusterIds;
    public static AiRecommendationPayload from(RecommendationRequestDto req) {
        return AiRecommendationPayload.builder()
                .date(req.getDate().toString())
                .startTime(req.getStartTime().toString())
                .endTime(req.getEndTime().toString())
                .foodCategoryId(req.getFoodCategoryId())
                .spotId(req.getSpotId())
                .clusterIds(req.getClusterIds())
                .build();
    }

}
