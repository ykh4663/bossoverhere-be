package com.dontgojunbao.bossoverhere.domain.recommendation.dto.response;


import com.dontgojunbao.bossoverhere.domain.recommendation.domain.RecommendationSegment;
import com.dontgojunbao.bossoverhere.domain.spot.dto.SpotDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@Schema(description = "추천 플랜 이동 구간 응답")
public class RecommendationResponse {
    @Schema(description = "이동 시각 (HH:mm)", example = "09:00")
    private String time;

    @Schema(description = "출발지 스팟 정보")
    private SpotDto from;

    @Schema(description = "도착지 스팟 정보")
    private SpotDto to;

    public static RecommendationResponse fromEntity(RecommendationSegment seg) {
        return new RecommendationResponse(
                seg.getTime(),
                SpotDto.from(seg.getFrom()),
                SpotDto.from(seg.getTo())
        );
    }

    public static List<RecommendationResponse> fromEntityList(List<RecommendationSegment> segments) {
        return segments.stream()
                .map(RecommendationResponse::fromEntity)
                .collect(Collectors.toList());
    }
}