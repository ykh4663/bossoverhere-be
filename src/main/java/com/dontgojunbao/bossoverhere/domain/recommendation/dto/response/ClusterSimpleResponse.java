package com.dontgojunbao.bossoverhere.domain.recommendation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "클러스터 간단 정보 응답")
public class ClusterSimpleResponse {
    @Schema(description = "클러스터 ID", example = "0")
    private Long id;

    @Schema(description = "클러스터 제목", example = "동네 일상과 젊음의 중심")
    private String title;

    @Schema(description = "클러스터 별칭", example = "활력 가득한 생활권 거점")
    private String nickname;
}