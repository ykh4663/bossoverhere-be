package com.dontgojunbao.bossoverhere.domain.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "AI 플랜 응답")
public class AiPlanResponse {
    @Schema(description = "이동 시각 (HH:mm)", example = "12:00")
    private String time;

    @Schema(description = "출발지 스팟 ID", example = "10")
    private Long fromSpotId;

    @Schema(description = "도착지 스팟 ID", example = "20")
    private Long toSpotId;
}