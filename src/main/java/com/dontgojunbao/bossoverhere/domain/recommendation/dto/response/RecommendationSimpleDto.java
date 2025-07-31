package com.dontgojunbao.bossoverhere.domain.recommendation.dto.response;

import com.dontgojunbao.bossoverhere.domain.recommendation.domain.RecommendationRequest;
import com.dontgojunbao.bossoverhere.domain.spot.dto.SpotDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Schema(description = "추천 간단 응답")
public class RecommendationSimpleDto {
    @Schema(description = "추천 요청 ID", example = "123")
    private Long requestId;

    @Schema(description = "추천 요청 날짜 (YYYY-MM-DD)", example = "2025-07-30")
    private LocalDate date;

    @Schema(description = "선택된 음식 카테고리 정보")
    private FoodCategoryDto foodCategory;

    @Schema(description = "추천 시작 스팟 정보")
    private SpotDto spot;

    public static RecommendationSimpleDto fromEntity(RecommendationRequest req) {
        return new RecommendationSimpleDto(
                req.getId(),
                req.getDate(),
                FoodCategoryDto.from(req.getFoodCategory()),
                SpotDto.from(req.getSpot())
        );
    }
}