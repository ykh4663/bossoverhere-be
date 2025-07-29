package com.dontgojunbao.bossoverhere.domain.recommendation.dto.response;


import com.dontgojunbao.bossoverhere.domain.spot.dto.SpotDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@Schema(description = "추천 플랜 응답")
public class RecommendationSegmentDto {
    @Schema(description = "이동 시각 (HH:mm)", example = "09:00")
    private String time;

    @Schema(description = "출발지 스팟 정보")
    private SpotDto from;

    @Schema(description = "도착지 스팟 정보")
    private SpotDto to;
}